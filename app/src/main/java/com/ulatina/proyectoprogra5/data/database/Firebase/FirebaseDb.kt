package com.ulatina.proyectoprogra5.data.database.Firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ulatina.proyectoprogra5.data.database.model.EjerciciosFirebase
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase

import javax.inject.Inject

class FirebaseDb @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    private val folder1 = "Usuarios"
    private val folder2 = "Datos"
    private val usuario: String
        get() = auth.currentUser?.email ?: throw IllegalStateException("Usuario no autenticado")

    fun saveUsuario(obj: UsuarioFirebase) {
        val folder = if (obj.id.isEmpty()) {
            firestore.collection(folder1).document(usuario).collection(folder2).document()
                .also { obj.id = it.id }
        } else {
            firestore.collection(folder1).document(usuario)
        }
        folder.set(obj)
            .addOnFailureListener { e -> Log.e("Firestore", "Error al guardar usuario", e) }

    }
    fun deleteRutina(usuarios: UsuarioFirebase, rutinaId: Long) {
        if (usuarios.id.isNotEmpty()) {
            if (usuarios.rutinas.isNotEmpty()) {
                val updatedRutinas = usuarios.rutinas.filterNot { it.id == rutinaId }
                firestore.collection(folder1)
                    .document(usuario)
                    .collection(folder2)
                    .document(usuarios.id)
                    .update("rutinas", updatedRutinas)
                    .addOnSuccessListener {
                        Log.d("DeleteRutina", "Rutina eliminada ")
                    }
                    .addOnFailureListener { e ->
                        Log.e("DeleteRutina", "Error al eliminar rutina: ${e.message}")
                    }
            }
        }
    }

    fun getDeets(): MutableLiveData<List<UsuarioFirebase>> {
        val deets = MutableLiveData<List<UsuarioFirebase>>()
        firestore.collection(folder1)
            .document(usuario)
            .collection(folder2)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirebaseDb", "Error getting usuarios", error)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val lista = it.documents.mapNotNull { doc ->
                        doc.toObject(UsuarioFirebase::class.java)
                    }
                    deets.value = lista
                }
            }
        return deets
    }


    fun deleteAll(){
        val resultLiveData = MutableLiveData<Boolean>()

        val ref = firestore
            .collection(folder1)
            .document(usuario)
            .collection(folder2)

        ref.get()
            .addOnSuccessListener { querySnapshot ->
                val batch = firestore.batch()

                querySnapshot.documents.forEach { document ->
                    batch.delete(document.reference)
                }

                batch.commit()
                    .addOnSuccessListener {
                        Log.d("DeleteAll", "eliminados exitosamente")
                        resultLiveData.value = true
                    }
                    .addOnFailureListener { e ->
                        Log.e("DeleteAll", "Error al eliminar objeto: ${e.message}")
                        resultLiveData.value = false
                    }
            }
            .addOnFailureListener { e ->
                Log.e("DeleteAll", "Error al obtener items para eliminar: ${e.message}")
                resultLiveData.value = false
            }


    }
}