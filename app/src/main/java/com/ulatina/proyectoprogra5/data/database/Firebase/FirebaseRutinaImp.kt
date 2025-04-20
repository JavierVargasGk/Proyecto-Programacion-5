package com.ulatina.proyectoprogra5.data.database.Firebase


import androidx.lifecycle.MutableLiveData
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase
import javax.inject.Inject

class FirebaseRepo  @Inject constructor (
    private val db: FirebaseDb ) {
    fun getAll() : MutableLiveData<List<UsuarioFirebase>>
    {
        return db.getDeets()
    }
    suspend fun insertOrUpdate(item: UsuarioFirebase)
    {
        db.saveUsuario(item)
    }

    suspend fun deleteAll()
    {
        db.deleteAll()
    }
    suspend fun deleteRutinas(item: UsuarioFirebase, rutinaId: Long)
    {
        db.deleteRutina(item,rutinaId)
    }
}