package com.example.deber_02db.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_02db.R
import com.example.deber_02db.model.Auto
import com.example.deber_02db.repository.AutoRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*

class AutoFormActivity : AppCompatActivity(), OnMapReadyCallback {

    // Declaración de las variables necesarias para los campos de la interfaz y el repositorio
    private lateinit var autoRepository: AutoRepository
    private lateinit var etNombre: EditText
    private lateinit var etMarca: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etFechaFabricacion: EditText
    private lateinit var btnGuardar: Button
    private lateinit var tvCoordenadas: TextView

    // Variables para manejar el mapa
    private lateinit var googleMap: GoogleMap
    private var selectedLat: Double = 0.0
    private var selectedLng: Double = 0.0

    // Formato para la fecha de fabricación
    private val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_form)

        // Inicialización de los componentes de la interfaz
        autoRepository = AutoRepository(this)
        etNombre = findViewById(R.id.etNombreAuto)
        etMarca = findViewById(R.id.etMarcaAuto)
        etPrecio = findViewById(R.id.etPrecioAuto)
        etFechaFabricacion = findViewById(R.id.etFechaFabricacion)
        btnGuardar = findViewById(R.id.btnGuardarAuto)
        tvCoordenadas = findViewById(R.id.tvCoordenadas)

        // Verificación de si se está editando un auto existente
        val autoId = intent.getIntExtra("autoId", -1)
        if (autoId != -1) {
            cargarAuto(autoId)
        }

        // Inicialización del mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Acción para guardar el auto cuando se presiona el botón
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                val nombre = etNombre.text.toString()
                val marca = etMarca.text.toString()
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val fechaFabricacion = try {
                    dateFormat.parse(etFechaFabricacion.text.toString()) ?: Date()
                } catch (e: Exception) {
                    Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Verificación de si se ha seleccionado una ubicación en el mapa
                if (selectedLat == 0.0 && selectedLng == 0.0) {
                    Toast.makeText(this, "Por favor selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Crear el objeto Auto con los datos proporcionados
                val auto = Auto(
                    id = if (autoId != -1) autoId else 0, // Evita null en ID
                    nombre = nombre,
                    marca = marca,
                    precio = precio,
                    fechaFabricacion = fechaFabricacion,
                    latitud = selectedLat,
                    longitud = selectedLng
                )

                // Guardar o actualizar el auto en la base de datos
                val resultado = if (autoId != -1) {
                    autoRepository.actualizarAuto(auto) // Actualizar auto existente
                } else {
                    autoRepository.insertarAuto(auto) // Insertar nuevo auto
                }
                val resultadoInt = resultado.toInt()  // Convertir Long a Int

                // Mostrar mensaje de éxito o error
                if (resultadoInt > 0 ) {
                    Toast.makeText(this, "Auto guardado con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar el auto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Metodo que se ejecuta cuando el mapa está listo
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Establecer la ubicación inicial en el mapa
        val ubicacionInicial = LatLng(-0.2096357, -78.4882838)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionInicial, 20f))

        // Acción cuando el usuario selecciona una ubicación en el mapa
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear() // Limpiar el mapa de marcadores anteriores
            googleMap.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))
            selectedLat = latLng.latitude
            selectedLng = latLng.longitude

            // Actualizar las coordenadas en la interfaz
            tvCoordenadas.text = "Latitud: $selectedLat, Longitud: $selectedLng"
        }
    }

    // Metodo para actualizar la ubicación en el mapa
    private fun actualizarUbicacionEnMapa(lat: Double, lng: Double) {
        if (lat == 0.0 || lng == 0.0) {
            Toast.makeText(this, "Coordenadas no válidas", Toast.LENGTH_SHORT).show()
            return
        }
        val ubicacion = LatLng(lat, lng)

        // Asegúrate de no borrar el mapa entero, solo agrega o actualiza el marcador
        googleMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación guardada"))

        // Mueve la cámara al marcador y haz un zoom en la ubicación
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16f))
    }

    // Cargar los datos de un auto existente en los campos de la interfaz
    private fun cargarAuto(autoId: Int) {
        val auto = autoRepository.obtenerPorId(autoId) ?: return run {
            Toast.makeText(this, "Error: Auto no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Asignar los valores del auto a los campos de la interfaz
        etNombre.setText(auto.nombre)
        etMarca.setText(auto.marca)
        etPrecio.setText(auto.precio.toString())
        etFechaFabricacion.setText(dateFormat.format(auto.fechaFabricacion))
        selectedLat = auto.latitud ?: 0.0  // Si es null, asigna 0.0
        selectedLng = auto.longitud ?: 0.0
        tvCoordenadas.text = "Latitud: $selectedLat, Longitud: $selectedLng"

        // Si el mapa está inicializado, actualizamos la ubicación en el mapa
        if (::googleMap.isInitialized) {
            actualizarUbicacionEnMapa(selectedLat, selectedLng)
        }
    }

    // Validar los campos del formulario antes de guardar el auto
    private fun validarCampos(): Boolean {
        return when {
            etNombre.text.isBlank() -> {
                etNombre.error = "El nombre es obligatorio"
                false
            }
            etMarca.text.isBlank() -> {
                etMarca.error = "La marca es obligatoria"
                false
            }
            etPrecio.text.isBlank() || etPrecio.text.toString().toDoubleOrNull() == null -> {
                etPrecio.error = "Ingrese un precio válido"
                false
            }
            etFechaFabricacion.text.isBlank() -> {
                etFechaFabricacion.error = "La fecha es obligatoria"
                false
            }
            else -> true
        }
    }
}
