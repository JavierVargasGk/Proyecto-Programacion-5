package com.ulatina.proyectoprogra5.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val id : String = "",
    val name : String = "",
    val rutinas : List<Rutina>,
    val isSelected : Boolean = false)