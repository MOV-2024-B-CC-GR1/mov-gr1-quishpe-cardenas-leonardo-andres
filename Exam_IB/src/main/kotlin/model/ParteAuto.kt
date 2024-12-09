package model

import kotlinx.serialization.Serializable

@Serializable
data class ParteAuto(
    var id: Int,
    var nombreParte: String,
    var cantidad: Int,
    var precio: Double,
    var fechaReemplazo: String // Ahora es un String
)
