package com.ulatina.proyectoprogra5.data.database.model

data class UsuarioFirebase (
        var id : String = "",
        var name : String = "",
        var edad: Long = 0,
        var peso: Long = 0,
        var nivelActividadFisica : Long = 0,
        var isSelected : Boolean = false
    ) {
        constructor() : this("","",0,0,0,false)
    }
data class RutinaFirebase(
    var id : String ="",
    var idUsuario : Long = 0,
    var name : String = "",
    var isSelected : Boolean = false
){
    constructor() : this ("",0,"",false)
}
data class EjerciciosFirebase(
    var idRutina: Long =0,
    var id: String = "",
    var name : String = "",
    var reps : Long = 0,
    var peso : Long = 0,
    var isSelected : Boolean = false
){
    constructor() : this(0,"","",0,0,false)
}