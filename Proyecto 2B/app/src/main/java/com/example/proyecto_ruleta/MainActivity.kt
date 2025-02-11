package com.example.proyecto_ruleta

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var optionsContainer: LinearLayout
    private val optionsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        optionsContainer = findViewById(R.id.optionsContainer)

        findViewById<Button>(R.id.btnAddOption).setOnClickListener {
            addOptionField()
        }

        findViewById<Button>(R.id.btnGenerateRoulette).setOnClickListener {
            createRoulette()
        }
    }

    private fun addOptionField() {
        val editText = EditText(this).apply {
            hint = "Opción ${optionsContainer.childCount + 1}"
            layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT,
                WRAP_CONTENT
            ).apply { topMargin = 8.dpToPx() }
        }
        optionsContainer.addView(editText)
    }

    private fun createRoulette() {
        optionsList.clear()
        for (i in 0 until optionsContainer.childCount) {
            val child = optionsContainer.getChildAt(i) as EditText
            child.text.toString().takeIf { it.isNotEmpty() }?.let {
                optionsList.add(it)
            }
        }

        if (optionsList.size > 1) {
            val intent = Intent(this, RouletteActivity::class.java).apply {
                putStringArrayListExtra("OPTIONS", ArrayList(optionsList))
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Agrega al menos 2 opciones", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}