package com.example.deber_02db.model

import java.util.Date

data class Auto(
    val id: Int,                     // Identificador único del auto
    var nombre: String,              // Nombre del auto
    var marca: String,               // Marca del auto
    var precio: Double,              // Precio del auto
    val fechaFabricacion: Date,
    val latitud: Double? = null,
    val longitud: Double? = null
) {
    override fun toString(): String {
        return "Auto: $nombre\nMarca: $marca\nPrecio: $$precio\nFecha de Fabricación: $fechaFabricacion\nLatitud: $latitud\n Longitud: $longitud"
    }
}