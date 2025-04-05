package com.ulatina.proyectoprogra5.data.database.model


import androidx.room.PrimaryKey


data class Rutina(
    @PrimaryKey(autoGenerate = true)
    var id : Long =0,
    var name : String = "",
    var ejercicios : List<Ejercicios>,
    var isSelected : Boolean = false
)