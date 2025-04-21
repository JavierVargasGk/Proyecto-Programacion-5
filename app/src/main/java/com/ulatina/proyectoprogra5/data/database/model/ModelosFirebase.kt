package com.ulatina.proyectoprogra5.data.database.model

data class UsuarioFirebase (
        var id : String = "",
        var name : String = "",
        var rutinas: List<RutinaFirebase>,
        var isSelected : Boolean = false
    ) {
        constructor() : this("","",listOf(),false)
    }
data class RutinaFirebase(
    var id : String = "",
    var name : String = "",
    var ejercicios: List<EjerciciosFirebase>,
    var isSelected : Boolean = false
){
    constructor() : this("", "", listOf(), false )
}
data class EjerciciosFirebase(
    var id: String = "",
    var name : String = "",
    var reps : Long = 0,
    var peso : Long = 0,
    var isSelected : Boolean = false
)