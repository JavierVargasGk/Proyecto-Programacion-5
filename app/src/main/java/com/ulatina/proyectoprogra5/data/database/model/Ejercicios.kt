package com.ulatina.proyectoprogra5.data.database.model



data class Ejercicios(
        var name : String = "",
        var reps : Long = 0,
        var peso : Long = 0,
        var isSelected : Boolean = false
)