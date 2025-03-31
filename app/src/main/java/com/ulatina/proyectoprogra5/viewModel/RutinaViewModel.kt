package com.ulatina.proyectoprogra5.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulatina.proyectoprogra5.data.database.model.Usuario
import com.ulatina.proyectoprogra5.data.database.repository.UsuarioRepository

import kotlinx.coroutines.launch

class RutinaViewModel(
    private val repository : UsuarioRepository
) : ViewModel() {
    val allItems: LiveData<List<Usuario>> get() = repository.getAll()

    fun insert(item: Usuario) = viewModelScope.launch {
        repository.insert(item);
    }
    fun update(item: Usuario) = viewModelScope.launch {
        repository.update(item);
    }
    fun deleteItem(item: Usuario) = viewModelScope.launch {
        repository.delete(item)
    }
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}