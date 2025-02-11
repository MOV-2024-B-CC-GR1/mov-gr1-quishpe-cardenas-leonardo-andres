package com.example.deber_02db.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_02db.R


class MainActivity : AppCompatActivity() {
    private lateinit var btnVerAutos: Button
    private lateinit var btnAgregarAuto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVerAutos = findViewById(R.id.btnVerAutos)
        btnAgregarAuto = findViewById(R.id.btnAgregarAuto)

        btnVerAutos.setOnClickListener {
            val intent = Intent(this, AutoListActivity::class.java)
            startActivity(intent)
        }

        btnAgregarAuto.setOnClickListener {
            val intent = Intent(this, AutoFormActivity::class.java)
            startActivity(intent)
        }
    }

    // Cargar el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    // Manejar las acciones del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_mapa -> {
                // Abrir la actividad del mapa con coordenadas de ejemplo
                val intent = Intent(this, MapaActivity::class.java)
                intent.putExtra("latitud", -0.180653)  // Latitud de ejemplo (Quito)
                intent.putExtra("longitud", -78.467834)  // Longitud de ejemplo (Quito)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
