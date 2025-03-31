package com.ulatina.proyectoprogra5.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id : Long =0,
    val name : String = "",
    val rutinas : Rutina,
    val isSelected : Boolean = false)