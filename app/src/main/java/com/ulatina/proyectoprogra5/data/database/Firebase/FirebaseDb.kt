package com.ulatina.proyectoprogra5.data.database.Firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase
import javax.inject.Inject

class FirebaseDb @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    private val folder1 = "Usuarios"
    private val folder2 = "RutinasDeUsuario"
    private val usuario: String
        get() = auth.currentUser?.email ?: throw IllegalStateException("Usuario no autenticado")

    fun saveDeets(usuarios: UsuarioFirebase) {
        val folder = if (usuarios.id.isEmpty()) {
            firestore.collection(folder1).document(usuario).collection(folder2).document()
                .also { usuarios.id = it.id }
        } else {
            firestore.collection(folder1).document(usuario).collection(folder2).document()
        }
        folder.set(usuarios)
            .addOnFailureListener { e -> Log.e("Firestore", "Error al guardar doc", e) }
    }

    fun deleteDeets(usuarios: UsuarioFirebase) {
        if (usuarios.id.isNotEmpty()) {
            firestore.collection(folder1).document(usuario).collection(folder2).document(usuarios.id)
                .delete().addOnSuccessListener {
                Log.d("DeleteObjeto", "Objeto eliminado")
            }
                .addOnCanceledListener {
                    Log.e("DeleteObjeto", "Objeto NO eliminado")
                }
        }
    }

    fun getDeets(): MutableLiveData<List<UsuarioFirebase>> {
        val deets = MutableLiveData<List<UsuarioFirebase>>()
        firestore.collection(folder1).document(usuario).collection(folder2)
            .addSnapshotListener { instant, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (instant != null){
                    val lista= ArrayList<UsuarioFirebase>()
                    instant.documents.forEach {
                        var i = it.toObject(UsuarioFirebase::class.java)
                        if (i != null){
                            lista.add(i)
                        }
                    }
                    deets.value= lista
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