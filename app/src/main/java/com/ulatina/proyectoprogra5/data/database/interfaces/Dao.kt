package com.ulatina.proyectoprogra5.data.database.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ulatina.proyectoprogra5.data.database.model.Ejercicio
import com.ulatina.proyectoprogra5.data.database.model.Rutina
import com.ulatina.proyectoprogra5.data.database.model.Usuario


@Dao
interface UsuarioDao {
    @Insert
    suspend fun insert(Item : Usuario)

    @Update
    suspend fun update(Item: Usuario)

    @Delete
    suspend fun  Delete(Item: Usuario)

    @Query("DELETE FROM usuarios")
    fun DropTableItems()

    @Query("select * FROM usuarios order by id")
    fun getAll(): LiveData<List<Usuario>>
}
@Dao
interface RutinasDao {
    @Insert
    suspend fun insert(Item : Rutina)

    @Update
    suspend fun update(Item: Rutina)

    @Delete
    suspend fun  Delete(Item: Rutina)

    @Query("DELETE FROM rutinas")
    fun DropTableItems()

    @Query("select * FROM rutinas order by id")
    fun getAll(): LiveData<List<Rutina>>
}
@Dao
interface EjerciciosDao {
    @Insert
    suspend fun insert(Item : Ejercicio)

    @Update
    suspend fun update(Item: Ejercicio)

    @Delete
    suspend fun  Delete(Item: Ejercicio)

    @Query("DELETE FROM ejercicios")
    fun DropTableItems()

    @Query("select * FROM ejercicios order by id")
    fun getAll(): LiveData<List<Rutina>>
}