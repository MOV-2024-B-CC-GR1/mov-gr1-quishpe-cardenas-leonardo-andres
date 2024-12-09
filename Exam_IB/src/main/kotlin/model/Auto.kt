package model

import kotlinx.serialization.Serializable

@Serializable
data class Auto(
    var id: Int,
    var nombre: String,
    var fechaFabricacion: String, // Ahora es un String
    var esElectrico: Boolean,
    var precio: Double
)
