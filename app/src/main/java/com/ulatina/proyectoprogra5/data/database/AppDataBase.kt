package com.ulatina.proyectoprogra5.data.database

import RutinasDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ulatina.proyectoprogra5.data.database.model.Rutina


@Database(entities = [Rutina::class], version = 0, exportSchema = false)
abstract class AppDataBase : RoomDatabase()
{

    abstract fun RutinasDao(): RutinasDao

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
                    "rutinas_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}