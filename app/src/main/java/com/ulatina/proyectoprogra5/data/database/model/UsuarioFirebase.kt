package com.ulatina.proyectoprogra5.data.database.model

data class UsuarioFirebase (
        var id : String = "",
        var name : String = "",
        var edad: Long = 0,
        var peso: Long = 0,
        var nivelActividadFisica : Long = 0,
        var rutinas : List<Rutina> = listOf(),
        var isSelected : Boolean = false
    ) {
        constructor() : this("","",0,0,0, listOf(),false) // Necesario para Firebase
    }
