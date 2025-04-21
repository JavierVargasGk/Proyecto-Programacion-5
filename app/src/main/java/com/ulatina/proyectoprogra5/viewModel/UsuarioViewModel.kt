package com.ulatina.proyectoprogra5.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulatina.proyectoprogra5.data.database.Firebase.FirebaseDb
import com.ulatina.proyectoprogra5.data.database.Firebase.FirebaseRepo
import com.ulatina.proyectoprogra5.data.database.model.EjerciciosFirebase
import com.ulatina.proyectoprogra5.data.database.model.RutinaFirebase
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val firebaseRepo: FirebaseRepo

) : ViewModel() {
    private val _usuarios = MutableLiveData<List<UsuarioFirebase>>()
    val usuarios: LiveData<List<UsuarioFirebase>> = _usuarios
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    private val _rutina = MutableLiveData<RutinaFirebase?>()
    val rutina: LiveData<RutinaFirebase?> = _rutina

    fun getUsuarios() {
        _loading.value = true
        _error.value = null
        firebaseRepo.getAll().observeForever { list ->
            _usuarios.value = list
            _loading.value = false
        }
    }
    fun insertOrUpdateUsuario(item: UsuarioFirebase) {
        _loading.value = true
        viewModelScope.launch {
            try {
                firebaseRepo.insertOrUpdate(item)
                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Error al hacer cambio o crear usuario ${e.message}"
                _loading.value = false
            }
        }
    }
    fun guardarEjercicio(userId: String, rutinaId: String, ejercicio: EjerciciosFirebase) {
        _loading.value = true
        firebaseRepo.guardarEjercicio(userId, rutinaId, ejercicio,
            onSuccess = {
                _loading.value = false
            },
            onFailure = { errorMessage ->
                _loading.value = false
                _error.value = errorMessage
            }
        )
    }
    fun getEjerciciosFromRutina(userId: String, rutinaId: String, onResult: (List<EjerciciosFirebase>) -> Unit, onError: (String) -> Unit) {
        firebaseRepo.getEjercicios(userId, rutinaId, onSuccess = { ejercicios ->
            onResult(ejercicios)
        }, onFailure = { errorMsg ->
            onError(errorMsg)
        })
    }
    fun deleteEjercicio(userId: String, rutinaId: String, ejercicioId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firebaseRepo.deleteEjercicio(userId, rutinaId, ejercicioId, onSuccess, onFailure)
    }
}