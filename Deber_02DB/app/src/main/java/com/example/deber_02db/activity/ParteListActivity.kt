package com.example.deber_02db.activity

import ParteListAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_02db.R
import com.example.deber_02db.model.Parte
import com.example.deber_02db.repository.ParteRepository

class ParteListActivity : AppCompatActivity() {
    private lateinit var parteRepository: ParteRepository
    private lateinit var lvPartes: ListView
    private lateinit var btnAgregarParte: Button
    private var partes = mutableListOf<Parte>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parte_list)

        parteRepository = ParteRepository(this)
        lvPartes = findViewById(R.id.lvPartes)
        btnAgregarParte = findViewById(R.id.btnAgregarParte)

        val autoId = intent.getIntExtra("autoId", -1)
        loadPartes(autoId)

        // Configurar clic en el botón de agregar parte
        btnAgregarParte.setOnClickListener {
            val intent = Intent(this, ParteFormActivity::class.java).apply {
                putExtra("autoId", autoId) // Pasa el ID del auto asociado
            }
            startActivity(intent) // Abre la actividad para agregar una nueva parte
        }

        lvPartes.setOnItemClickListener { _, _, position, _ ->
            val parteSeleccionada = partes[position]
            mostrarOpciones(parteSeleccionada, autoId)
        }
    }

    override fun onResume() {
        super.onResume()

        val autoId = intent.getIntExtra("autoId", -1)
        if (autoId != -1) {
            loadPartes(autoId)
        }
    }

    private fun loadPartes(autoId: Int) {
        partes.clear()
        partes.addAll(parteRepository.obtenerPorAuto(autoId))

        val adapter = ParteListAdapter(this, partes)
        lvPartes.adapter = adapter
    }

    private fun mostrarOpciones(parte: Parte, autoId: Int) {
        val opciones = arrayOf("Actualizar", "Eliminar")
        AlertDialog.Builder(this)
            .setTitle("Opciones para ${parte.nombreParte}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> { // Actualizar
                        val intent = Intent(this, ParteFormActivity::class.java).apply {
                            putExtra("parteId", parte.id)
                            putExtra("autoId", autoId)
                        }
                        startActivity(intent)
                    }
                    1 -> { // Eliminar
                        confirmarEliminacion(parte, autoId)
                    }
                }
            }
            .show()
    }

    private fun confirmarEliminacion(parte: Parte, autoId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Parte")
            .setMessage("¿Estás seguro de que deseas eliminar la parte '${parte.nombreParte}'?")
            .setPositiveButton("Sí") { _, _ ->
                val resultado = parteRepository.eliminarParte(parte.id)
                if (resultado > 0) {
                    Toast.makeText(this, "Parte eliminada", Toast.LENGTH_SHORT).show()
                    loadPartes(autoId)
                } else {
                    Toast.makeText(this, "Error al eliminar la parte", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}