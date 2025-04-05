package com.ulatina.proyectoprogra5.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ulatina.proyectoprogra5.data.database.Firebase.FirebaseRepo
import com.ulatina.proyectoprogra5.data.database.model.Usuario

import com.ulatina.proyectoprogra5.data.database.repository.UsuarioRepository

import kotlinx.coroutines.launch
import javax.inject.Inject

class UsuarioViewModel @Inject constructor(
    private val repository : UsuarioRepository,
    private val firebaseRepo: FirebaseRepo

) : ViewModel() {
    private val _usuarios = MutableLiveData<List<Usuario>>()
    val usuarios : LiveData<List<Usuario>> = _usuarios

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getUsuarios(remoteSource: Boolean): LiveData<List<Usuario>>{
        _loading.value = true
        _error.value = null
        return if (remoteSource){
            val liveData = MutableLiveData<List<Usuario>>()
            viewModelScope.launch {
                try {
                   firebaseRepo.getAll().observeForever{ firebaseUser ->
                       val convertedUser = firebaseUser.map { firebaseUser ->
                           Usuario(
                               id = firebaseUser.id,
                               name = firebaseUser.name,
                               edad = firebaseUser.edad,
                               nivelActividadFisica = firebaseUser.nivelActividadFisica,
                               rutinas = firebaseUser.rutinas,
                               isSelected = firebaseUser.isSelected
                           )
                       }
                       _usuarios.postValue(convertedUser)
                       liveData.postValue(convertedUser)
                   }
                } catch(e: Exception){
                    _error.postValue("Error: ${e.message}")
                    liveData.postValue(repository.getAll().value ?: emptyList())
                } finally {
                    _loading.postValue(false)
                }
            }
            liveData
        } else {
            repository.getAll()
        }
    }


}