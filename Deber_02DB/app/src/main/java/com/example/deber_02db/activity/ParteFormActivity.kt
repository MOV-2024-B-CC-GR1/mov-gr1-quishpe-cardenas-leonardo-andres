package com.example.deber_02db.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_02db.R
import com.example.deber_02db.model.Parte
import com.example.deber_02db.repository.ParteRepository
import java.text.SimpleDateFormat
import java.util.*

class ParteFormActivity : AppCompatActivity() {
    private lateinit var parteRepository: ParteRepository

    private lateinit var etNombreParte: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etFechaReemplazo: EditText
    private lateinit var btnGuardar: Button

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parte_form)

        parteRepository = ParteRepository(this)

        etNombreParte = findViewById(R.id.etNombreParte)
        etPrecio = findViewById(R.id.etPrecioParte)
        etFechaReemplazo = findViewById(R.id.etFechaReemplazo)
        btnGuardar = findViewById(R.id.btnGuardarParte)

        val autoId = intent.getIntExtra("autoId", -1)
        val parteId = intent.getIntExtra("parteId", -1)
        if (parteId != -1) {
            cargarParte(parteId)
        }

        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                val nombreParte = etNombreParte.text.toString()
                val precio = etPrecio.text.toString().toDouble()
                val fechaReemplazo = try {
                    dateFormat.parse(etFechaReemplazo.text.toString()) ?: Date()
                } catch (e: Exception) {
                    Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val parte = Parte(
                    id = if (parteId == -1) 0 else parteId,
                    idAuto = autoId,
                    nombreParte = nombreParte,
                    precio = precio,
                    fechaReemplazo = fechaReemplazo
                )

                val resultado = if (parteId == -1) {
                    parteRepository.insertarParte(parte) > 0
                } else {
                    parteRepository.actualizarParte(parte) > 0
                }

                if (resultado) {
                    Toast.makeText(this, "Parte guardada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar la parte", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cargarParte(parteId: Int) {
        val parte = parteRepository.obtenerPorId(parteId)
        parte?.let {
            etNombreParte.setText(it.nombreParte)
            etPrecio.setText(it.precio.toString())
            etFechaReemplazo.setText(dateFormat.format(parte.fechaReemplazo))
        }
    }

    private fun validarCampos(): Boolean {
        val nombreParte = etNombreParte.text.toString().trim()
        val precio = etPrecio.text.toString().trim()
        val fechaReemplazo = etFechaReemplazo.text.toString().trim()

        return when {
            nombreParte.isEmpty() -> {
                etNombreParte.error = "El nombre de la parte es obligatorio"
                false
            }
            precio.isEmpty() -> {
                etPrecio.error = "El precio es obligatorio"
                false
            }
            fechaReemplazo.isEmpty() -> {
                etFechaReemplazo.error = "La fecha de reemplazo es obligatoria"
                false
            }
            else -> true
        }
    }
}