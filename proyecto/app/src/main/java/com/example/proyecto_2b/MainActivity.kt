package com.example.proyecto_2b

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var ruletaView: View
    private lateinit var btnGirar: Button
    private lateinit var resultadoText: TextView
    private val opciones = listOf("Opción 1", "Opción 2", "Opción 3", "Opción 4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ruletaView = findViewById(R.id.ruletaView)
        btnGirar = findViewById(R.id.btnGirar)
        resultadoText = findViewById(R.id.resultadoText)

        btnGirar.setOnClickListener { girarRuleta() }
    }

    private fun girarRuleta() {
        val vueltas = Random.nextInt(5, 10)
        val anguloFinal = vueltas * 360 + Random.nextInt(0, 360)
        val opcionSeleccionada = opciones[(anguloFinal % 360) / (360 / opciones.size)]

        ObjectAnimator.ofFloat(ruletaView, "rotation", 0f, anguloFinal.toFloat()).apply {
            duration = 3000
            start()
            addUpdateListener {
                if (it.animatedFraction == 1.0f) {
                    resultadoText.text = "Resultado: $opcionSeleccionada"
                }
            }
        }
    }
}