package com.ulatina.proyectoprogra5.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulatina.proyectoprogra5.data.database.Firebase.FirebaseDb
import com.ulatina.proyectoprogra5.data.database.Firebase.FirebaseRepo
import com.ulatina.proyectoprogra5.data.database.model.UsuarioFirebase
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsuarioViewModel @Inject constructor(
    private val firebaseRepo: FirebaseRepo

) : ViewModel() {
    private val _usuarios = MutableLiveData<List<UsuarioFirebase>>()
    val usuarios: LiveData<List<UsuarioFirebase>> = _usuarios
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

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
    fun deleteRutinas(rutinaId:Long, item: UsuarioFirebase) {
        _loading.value = true
        viewModelScope.launch {
            try {
                firebaseRepo.deleteRutinas(item,rutinaId)
                _loading.value = false
            } catch (e: Exception) {
                _error.value = "Fallo al borrar${e.message}"
                _loading.value = false
            }
        }
    }



}