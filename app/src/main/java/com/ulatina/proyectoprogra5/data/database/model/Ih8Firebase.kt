package com.ulatina.proyectoprogra5.data.database.model

data class usuarioWithRoutina(
    var usuario: UsuarioFirebase,
    var routina: List<RutinaFirebase>
        )
data class routinaWithEjercicio(
    var routina: RutinaFirebase,
    var ejerciciosFirebase: List<EjerciciosFirebase>
)