package com.ulatina.proyectoprogra5.data.database.model


import androidx.room.PrimaryKey


data class Rutina(
    @PrimaryKey(autoGenerate = true)
    val id : Long =0,
    val name : String = "",
    val ejercicios : List<Ejercicios>,
    val isSelected : Boolean = false
)