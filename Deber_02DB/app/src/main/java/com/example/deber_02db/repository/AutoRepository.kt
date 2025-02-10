package com.example.deber_02db.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.deber_02db.database.Database
import com.example.deber_02db.model.Auto
import java.text.SimpleDateFormat
import java.util.*

class AutoRepository(context: Context) {
    private val dbHelper = Database(context)
    private val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())  // Solo año

    fun insertarAuto(auto: Auto): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", auto.nombre)
            put("marca", auto.marca)
            put("precio", auto.precio)
            put("fecha_fabricacion", dateFormat.format(auto.fechaFabricacion))
            put("latitud", auto.latitud ?: 0.0)  // Maneja valores nulos
            put("longitud", auto.longitud ?: 0.0)
        }
        return try {
            val result = db.insert("Auto", null, values)
            if (result == -1L) {
                throw Exception("Error al insertar en la base de datos")
            }
            result
        } catch (e: Exception) {
            e.printStackTrace()
            -1L  // Retorna -1 si hay un fallo
        } finally {
            db.close()
        }
    }

    fun actualizarAuto(auto: Auto): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", auto.nombre)
            put("marca", auto.marca)
            put("precio", auto.precio)
            put("fecha_fabricacion", dateFormat.format(auto.fechaFabricacion))
            put("latitud", auto.latitud)  // Agregado
            put("longitud", auto.longitud)  // Agregado
        }
        return try {
            db.update("Auto", values, "id = ?", arrayOf(auto.id.toString()))
        } finally {
            db.close()
        }
    }


    fun eliminarAuto(id: Int): Int {
        val db = dbHelper.writableDatabase
        return try {
            db.delete("Auto", "id = ?", arrayOf(id.toString()))
        } finally {
            db.close()
        }
    }

    fun obtenerTodos(): List<Auto> {
        val autos = mutableListOf<Auto>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM Auto", null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_fabricacion"))
                    val fechaFabricacion = try {
                        dateFormat.parse(fecha) ?: Date() // Si falla, usar la fecha actual
                    } catch (e: Exception) {
                        Date() // Fecha por defecto en caso de error
                    }

                    val auto = Auto(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        marca = cursor.getString(cursor.getColumnIndexOrThrow("marca")),
                        precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                        fechaFabricacion = fechaFabricacion
                    )
                    autos.add(auto)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return autos
    }

    fun obtenerPorId(id: Int): Auto? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Auto WHERE id = ?", arrayOf(id.toString()))
        var auto: Auto? = null

        try {
            if (cursor.moveToFirst()) {
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_fabricacion"))
                val fechaFabricacion = try {
                    dateFormat.parse(fecha) ?: Date() // Si falla, usar la fecha actual
                } catch (e: Exception) {
                    Date() // Fecha por defecto en caso de error
                }

                auto = Auto(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    marca = cursor.getString(cursor.getColumnIndexOrThrow("marca")),
                    precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                    fechaFabricacion = fechaFabricacion,
                    latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud")),  // Agregado
                    longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))  // Agregado
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor.close()
            db.close()
        }

        return auto
    }
}