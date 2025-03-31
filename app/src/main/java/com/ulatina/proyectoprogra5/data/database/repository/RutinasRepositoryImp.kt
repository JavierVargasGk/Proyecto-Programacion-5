package com.ulatina.proyectoprogra5.data.database.repository

import RutinasDao
import androidx.lifecycle.LiveData
import com.ulatina.proyectoprogra5.data.database.model.Rutina
import javax.inject.Inject

class RutinasRepository @Inject constructor(private val rutinasDao: RutinasDao)
{
    fun getAll() : LiveData<List<Rutina>>
    {
        return rutinasDao.getAll()
    }
    suspend fun insert(item: Rutina)
    {
        rutinasDao.insert(item)
    }
    suspend fun update(item: Rutina)
    {
        rutinasDao.update(item)
    }
    suspend fun  deleteAll()
    {
        rutinasDao.DropTableItems()
    }
    suspend fun delete(item: Rutina)
    {
        rutinasDao.Delete(item)
    }
}
