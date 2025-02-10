package com.example.deber_02db.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.deber_02db.database.Database
import com.example.deber_02db.model.Parte
import java.text.SimpleDateFormat
import java.util.*

class ParteRepository(context: Context) {
    private val dbHelper = Database(context)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun insertarParte(parte: Parte): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_auto", parte.idAuto)
            put("nombre_parte", parte.nombreParte)
            put("precio", parte.precio)
            put("fecha_reemplazo", dateFormat.format(parte.fechaReemplazo))
        }
        return try {
            db.beginTransaction()
            val id = db.insert("Parte", null, values)
            db.setTransactionSuccessful()
            id
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun eliminarParte(id: Int): Int {
        val db = dbHelper.writableDatabase
        return try {
            db.beginTransaction()
            val result = db.delete("Parte", "id = ?", arrayOf(id.toString()))
            db.setTransactionSuccessful()
            result
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun obtenerPorAuto(idAuto: Int): List<Parte> {
        val partes = mutableListOf<Parte>()
        val db = dbHelper.readableDatabase

        try {
            val cursor = db.query(
                "Parte",
                null,
                "id_auto = ?",
                arrayOf(idAuto.toString()),
                null,
                null,
                null
            )

            cursor.use {
                if (it.moveToFirst()) {
                    do {
                        val parte = Parte(
                            id = it.getInt(it.getColumnIndexOrThrow("id")),
                            idAuto = idAuto,
                            nombreParte = it.getString(it.getColumnIndexOrThrow("nombre_parte")),
                            precio = it.getDouble(it.getColumnIndexOrThrow("precio")),
                            fechaReemplazo = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("fecha_reemplazo"))) ?: Date()
                        )
                        partes.add(parte)
                    } while (it.moveToNext())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return partes
    }

    fun obtenerPorId(id: Int): Parte? {
        val db = dbHelper.readableDatabase
        var parte: Parte? = null

        try {
            val cursor = db.query(
                "Parte",
                null,
                "id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )

            cursor.use {
                if (it.moveToFirst()) {
                    parte = Parte(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        idAuto = it.getInt(it.getColumnIndexOrThrow("id_auto")),
                        nombreParte = it.getString(it.getColumnIndexOrThrow("nombre_parte")),
                        precio = it.getDouble(it.getColumnIndexOrThrow("precio")),
                        fechaReemplazo = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("fecha_reemplazo"))) ?: Date()
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return parte
    }

    fun actualizarParte(parte: Parte): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_auto", parte.idAuto)
            put("nombre_parte", parte.nombreParte)
            put("precio", parte.precio)
            put("fecha_reemplazo", dateFormat.format(parte.fechaReemplazo))
        }

        return try {
            db.beginTransaction()
            val rowsAffected = db.update(
                "Parte",
                values,
                "id = ?",
                arrayOf(parte.id.toString())
            )
            db.setTransactionSuccessful()
            rowsAffected
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}