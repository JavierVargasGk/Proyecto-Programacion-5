package com.ulatina.proyectoprogra5.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
//TODO HACER RUTINAS UN CONVERTIR A GSON
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    var id : String = "",
    var name : String = "",
    var edad: Long = 0,
    var peso : Long = 0,
    var nivelActividadFisica : Long = 0,
    var isSelected : Boolean = false)
@Entity
data class Rutina(
    @PrimaryKey(autoGenerate = true)
    var id : Long =0,
    var idUsuario : Long = 0,
    var name : String = "",
    var isSelected : Boolean = false
)
@Entity
data class Ejercicio(
    @PrimaryKey(autoGenerate = true)
    var idRutina: Long =0,
    var id: Long = 0,
    var name : String = "",
    var reps : Long = 0,
    var peso : Long = 0,
    var isSelected : Boolean = false
)