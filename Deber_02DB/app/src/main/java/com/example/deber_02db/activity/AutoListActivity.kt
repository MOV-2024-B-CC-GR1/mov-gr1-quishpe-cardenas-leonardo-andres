package com.example.deber_02db.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_02db.R
import com.example.deber_02db.model.Auto
import com.example.deber_02db.repository.AutoRepository

class AutoListActivity : AppCompatActivity() {
    private lateinit var autoRepository: AutoRepository
    private lateinit var lvAutos: ListView
    private lateinit var btnAgregarAuto: Button
    private var autos = mutableListOf<Auto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_list)

        autoRepository = AutoRepository(this)
        lvAutos = findViewById(R.id.lvAutos)
        btnAgregarAuto = findViewById(R.id.btnAgregarAuto)

        // Cargar lista de autos
        loadAutos()

        // Configurar clic en el botón de agregar auto
        btnAgregarAuto.setOnClickListener {
            val intent = Intent(this, AutoFormActivity::class.java)
            startActivity(intent) // Abre la actividad para agregar un nuevo auto
        }

        // Configurar clic en un elemento de la lista
        lvAutos.setOnItemClickListener { _, _, position, _ ->
            val autoSeleccionado = autos[position]
            mostrarOpciones(autoSeleccionado)
        }
    }

    override fun onResume() {
        super.onResume()
        loadAutos() // Recargar la lista al volver a esta actividad
    }

    private fun loadAutos() {
        autos.clear()
        autos.addAll(autoRepository.obtenerTodos())

        val adapter = AutoListAdapter(this, autos)
        lvAutos.adapter = adapter
    }


    private fun mostrarOpciones(auto: Auto) {
        // Mostrar un cuadro de diálogo con las opciones Actualizar, Eliminar y Ver Partes
        val opciones = arrayOf("Actualizar", "Eliminar", "Ver Partes")
        AlertDialog.Builder(this)
            .setTitle("Opciones para ${auto.nombre}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> { // Actualizar
                        val intent = Intent(this, AutoFormActivity::class.java).apply {
                            putExtra("autoId", auto.id)
                        }
                        startActivity(intent)
                    }
                    1 -> { // Eliminar
                        confirmarEliminacion(auto)
                    }
                    2 -> { // Ver Partes
                        val intent = Intent(this, ParteListActivity::class.java).apply {
                            putExtra("autoId", auto.id)
                        }
                        startActivity(intent)
                    }
                }
            }
            .show()
    }

    private fun confirmarEliminacion(auto: Auto) {
        // Mostrar un cuadro de confirmación para eliminar el auto
        AlertDialog.Builder(this)
            .setTitle("Eliminar Auto")
            .setMessage("¿Estás seguro de que deseas eliminar el auto '${auto.nombre}'?")
            .setPositiveButton("Sí") { _, _ ->
                val resultado = autoRepository.eliminarAuto(auto.id)
                if (resultado > 0) {
                    Toast.makeText(this, "Auto eliminado", Toast.LENGTH_SHORT).show()
                    loadAutos() // Recargar la lista
                } else {
                    Toast.makeText(this, "Error al eliminar el auto", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
