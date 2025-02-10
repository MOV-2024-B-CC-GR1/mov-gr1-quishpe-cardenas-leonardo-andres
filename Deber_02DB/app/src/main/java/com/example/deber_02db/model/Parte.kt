package com.example.deber_02db.model

import java.util.Date

data class Parte(
    val id: Int,                     // Identificador único de la parte
    var idAuto: Int,                 // ID del auto asociado (clave foránea)
    var nombreParte: String,         // Nombre de la parte del auto
    var precio: Double,              // Precio de la parte del auto
    val fechaReemplazo: Date         // Fecha de reemplazo de la parte
) {
    override fun toString(): String {
        return "Parte: $nombreParte\nPrecio: $$precio\nFecha de Reemplazo: $fechaReemplazo"
    }
}
