package com.ulatina.proyectoprogra5.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulatina.proyectoprogra5.data.database.model.Rutina
import com.ulatina.proyectoprogra5.data.database.repository.RutinasRepository
import kotlinx.coroutines.launch

class RutinaViewModel(
    private val repository : RutinasRepository
) : ViewModel() {
    val allItems: LiveData<List<Rutina>> get() = repository.getAll()

    fun insert(item: Rutina) = viewModelScope.launch {
        repository.insert(item);
    }
    fun update(item: Rutina) = viewModelScope.launch {
        repository.update(item);
    }
    fun deleteItem(item: Rutina) = viewModelScope.launch {
        repository.delete(item)
    }
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}