package com.example.proyecto_ruleta

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class RouletteActivity : AppCompatActivity() {
    private lateinit var rouletteWheel: RouletteWheelView
    private var isSpinning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roulette)

        rouletteWheel = findViewById(R.id.rouletteWheel)
        val options = intent.getStringArrayListExtra("OPTIONS") ?: return finish()

        rouletteWheel.setupWheelOptions(options)

        findViewById<Button>(R.id.btnSpin).setOnClickListener {
            if (!isSpinning) {
                spinRoulette()
            }
        }
    }

    private fun spinRoulette() {
        isSpinning = true
        val random = Random
        val extraRotations = 5
        val targetAngle = 360 * extraRotations + random.nextFloat() * 360

        rouletteWheel.rotateWheel(targetAngle, 5000)

        Handler(Looper.getMainLooper()).postDelayed({
            isSpinning = false
            val selectedIndex = calculateSelectedIndex(targetAngle)
            showResult(selectedIndex)
        }, 5000)
    }

    private fun calculateSelectedIndex(targetAngle: Float): Int {
        val normalizedAngle = targetAngle % 360
        val sectionAngle = 360f / rouletteWheel.options.size
        return (normalizedAngle / sectionAngle).toInt()
    }

    private fun showResult(index: Int) {
        val option = rouletteWheel.options[index]
        AlertDialog.Builder(this)
            .setTitle("Resultado")
            .setMessage("Ha salido: $option")
            .setPositiveButton("OK", null)
            .show()
    }
}