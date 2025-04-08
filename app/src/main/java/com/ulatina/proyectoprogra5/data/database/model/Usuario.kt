package com.ulatina.proyectoprogra5.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    var id : String = "",
    var name : String = "",
    var edad: Long = 0,
    var peso : Long = 0,
    var nivelActividadFisica : Long = 0,
    var rutinas : List<Rutina>,
    var isSelected : Boolean = false)