package com.example.proyecto_2b

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConfigurarRuletaActivity : AppCompatActivity() {
    private lateinit var etNuevaOpcion: EditText
    private lateinit var btnAgregarOpcion: Button
    private lateinit var btnGuardarRuleta: Button
    private lateinit var layoutOpciones: LinearLayout
    private val opciones = mutableListOf<String>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurar_ruleta)

        etNuevaOpcion = findViewById(R.id.etNuevaOpcion)
        btnAgregarOpcion = findViewById(R.id.btnAgregarOpcion)
        btnGuardarRuleta = findViewById(R.id.btnGuardarRuleta)
        layoutOpciones = findViewById(R.id.layoutOpciones)
        sharedPreferences = getSharedPreferences("RuletaPrefs", MODE_PRIVATE)

        btnAgregarOpcion.setOnClickListener {
            val opcion = etNuevaOpcion.text.toString()
            if (opcion.isNotEmpty()) {
                opciones.add(opcion)
                etNuevaOpcion.text.clear()
                actualizarListaOpciones()
            }
        }

        btnGuardarRuleta.setOnClickListener {
            if (opciones.isNotEmpty()) {
                sharedPreferences.edit().putStringSet("opcionesRuleta", opciones.toSet()).apply()
                Toast.makeText(this, "Ruleta guardada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun actualizarListaOpciones() {
        layoutOpciones.removeAllViews()
        opciones.forEach { opcion ->
            val textView = TextView(this)
            textView.text = opcion
            layoutOpciones.addView(textView)
        }
    }
}