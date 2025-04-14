package com.ulatina.proyectoprogra5.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ulatina.proyectoprogra5.data.database.interfaces.UsuarioDao
import com.ulatina.proyectoprogra5.data.database.model.Rutina


@Database(entities = [Rutina::class], version = 0, exportSchema = false)
abstract class AppDataBase : RoomDatabase()
{

    abstract fun UsuarioDao(): UsuarioDao

    companion object
    {
        @Volatile
        private var INSTANCE: AppDataBase?=null

        fun getDatabase(context: Context): AppDataBase
        {
            return INSTANCE?: synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "usuario_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}