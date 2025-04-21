package com.ulatina.proyectoprogra5.data.database.Firebase


import androidx.lifecycle.MutableLiveData
import com.ulatina.proyectoprogra5.data.database.model.EjerciciosFirebase
import com.ulatina.proyectoprogra5.data.database.model.RutinaFirebase
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase
import javax.inject.Inject

class FirebaseRepo  @Inject constructor (
    private val db: FirebaseDb ) {
    fun getAll() : MutableLiveData<List<UsuarioFirebase>>
    {
        return db.getDeets()
    }
    fun insertOrUpdate(item: UsuarioFirebase)
    {
        db.saveUsuario(item)
    }
    fun guardarEjercicio(userId: String, rutinaId: String, ejercicio: EjerciciosFirebase, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.guardarEjercicio(userId, rutinaId, ejercicio, onSuccess, onFailure)
    }
    fun getEjercicios(userId: String, rutinaId: String, onSuccess: (List<EjerciciosFirebase>) -> Unit, onFailure: (String) -> Unit
    ) {
        db.getRutina(userId, rutinaId, onSuccess, onFailure)
    }
    fun deleteEjercicio(userId: String, rutinaId: String, ejercicioId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
            db.deleteEjercicio(userId = userId, rutinaId = rutinaId, ejercicioId = ejercicioId, onSuccess = onSuccess, onFailure = onFailure)
    }

}