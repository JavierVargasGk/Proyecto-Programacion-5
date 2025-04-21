package com.ulatina.proyectoprogra5.data.database.Firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ulatina.proyectoprogra5.data.database.model.EjerciciosFirebase
import com.ulatina.proyectoprogra5.data.database.model.RutinaFirebase
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class FirebaseDb @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) {

    private val usuariosRef: DatabaseReference
        get() = database.getReference("Usuarios/${auth.currentUser?.uid ?: throw IllegalStateException("Usuario no autenticado")}")

    fun saveUsuario(obj: UsuarioFirebase) {
        if (obj.id.isEmpty()) {
            val newRef = usuariosRef.push()
            obj.id = newRef.key ?: ""
            newRef.setValue(obj)
        } else {
            usuariosRef.child(obj.id).setValue(obj)
        }
    }
    fun guardarEjercicio(
        userId: String,
        rutinaId: String,
        ejercicio: EjerciciosFirebase,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("Usuarios").child(userId).child(userId)
        userRef.get().addOnSuccessListener { snapshot ->
            val usuario = snapshot.getValue(UsuarioFirebase::class.java)
            if (usuario == null) {
                onFailure("Usuario no encontrado")
                return@addOnSuccessListener
            }
            val rutinas = usuario.rutinas?.toMutableList() ?: mutableListOf()
            val rutinaIndex = rutinas.indexOfFirst { it.id == rutinaId }
            val ejercicioId = userRef.push().key
            if (ejercicioId == null) {
                onFailure("No se pudo generar ID para el ejercicio")
                return@addOnSuccessListener
            }
            if (rutinaIndex == -1) {
                onFailure("Rutina no encontrada")
                return@addOnSuccessListener
            }
            ejercicio.id = ejercicioId
            val rutina = rutinas[rutinaIndex]
            val ejerciciosActualizados = rutina.ejercicios?.toMutableList() ?: mutableListOf()
            ejerciciosActualizados.add(ejercicio)
            rutinas[rutinaIndex] = rutina.copy(ejercicios = ejerciciosActualizados)
            val usuarioActualizado = usuario.copy(rutinas = rutinas)
            userRef.setValue(usuarioActualizado)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e.message ?: "Error al guardar ejercicio") }
        }.addOnFailureListener { e ->
            onFailure("Error al acceder a Firebase: ${e.message}")
        }
    }
    fun getRutina(
        userId: String,
        rutinaId: String,
        onSuccess: (List<EjerciciosFirebase>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val ref = FirebaseDatabase.getInstance()
            .reference
            .child("Usuarios")
            .child(userId)
            .child(userId)

        ref.get().addOnSuccessListener { snapshot ->
            val usuario = snapshot.getValue(UsuarioFirebase::class.java)
            if (usuario != null) {
                val rutina = usuario.rutinas?.find { it.id == rutinaId }
                if (rutina != null) {
                    onSuccess(rutina.ejercicios ?: emptyList())
                } else {
                    onFailure("Rutina no encontrada")
                }
            } else {
                onFailure("Usuario no encontrado")
            }
        }.addOnFailureListener { e ->
            onFailure(e.message ?: "Error al acceder a Firebase")
        }
    }






    fun deleteEjercicio(
        userId: String,
        rutinaId: String,
        ejercicioId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("Usuarios").child(userId).child(userId)

        userRef.get().addOnSuccessListener { snapshot ->
            val usuario = snapshot.getValue(UsuarioFirebase::class.java)
            if (usuario == null) {
                onFailure("Usuario no encontrado")
                return@addOnSuccessListener
            }

            val rutinas = usuario.rutinas?.toMutableList() ?: mutableListOf()
            val rutinaIndex = rutinas.indexOfFirst { it.id == rutinaId }
            if (rutinaIndex == -1) {
                onFailure("Rutina no encontrada")
                return@addOnSuccessListener
            }

            val rutina = rutinas[rutinaIndex]
            val ejerciciosActualizados = rutina.ejercicios?.filterNot { it.id == ejercicioId } ?: listOf()
            rutinas[rutinaIndex] = rutina.copy(ejercicios = ejerciciosActualizados)

            val usuarioActualizado = usuario.copy(rutinas = rutinas)
            userRef.setValue(usuarioActualizado)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e ->
                    onFailure("Error al eliminar ejercicio: ${e.message}")
                }

        }.addOnFailureListener { e ->
            onFailure("Error al acceder a Firebase: ${e.message}")
        }
    }

    fun getDeets(): MutableLiveData<List<UsuarioFirebase>> {
        val liveData = MutableLiveData<List<UsuarioFirebase>>()

        usuariosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuarios = snapshot.children.mapNotNull {
                    it.getValue(UsuarioFirebase::class.java)
                }
                liveData.value = usuarios
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseDb", "Error al obtener datos: ${error.message}")
            }
        })

        return liveData
    }

    fun deleteAll() {
        usuariosRef.removeValue()
            .addOnSuccessListener {
                Log.d("DeleteAll", "Datos eliminados correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("DeleteAll", "Error al eliminar datos: ${e.message}")
            }
    }
}
