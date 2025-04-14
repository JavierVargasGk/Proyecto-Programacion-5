package com.ulatina.proyectoprogra5.data.database.repository



import androidx.lifecycle.LiveData
import com.ulatina.proyectoprogra5.data.database.interfaces.UsuarioDao
import com.ulatina.proyectoprogra5.data.database.model.Usuario
import javax.inject.Inject

class UsuarioRepository @Inject constructor(private val usuarioDao: UsuarioDao)
{
    fun getAll() : LiveData<List<Usuario>>
    {
        return usuarioDao.getAll()
    }
    suspend fun insert(item: Usuario)
    {
        usuarioDao.insert(item)
    }
    suspend fun update(item: Usuario)
    {
        usuarioDao.update(item)
    }
    suspend fun deleteAll()
    {
        usuarioDao.DropTableItems()
    }
    suspend fun delete(item: Usuario)
    {
        usuarioDao.Delete(item)
    }
}
