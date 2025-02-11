package com.example.deber_02db.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_02db.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// Clase que representa la actividad del mapa, encargada de mostrar una ubicación específica en Google Maps.
class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    // Variables para almacenar las coordenadas de la ubicación
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Obtener las coordenadas que se enviaron a través del Intent desde otra actividad
        latitud = intent.getDoubleExtra("latitud", 0.0)
        longitud = intent.getDoubleExtra("longitud", 0.0)

        // Obtener el fragmento del mapa del layout y configurar el callback para cuando el mapa esté listo
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this) // Iniciar la carga del mapa
    }

    // Metodo que se ejecuta cuando el mapa está listo para ser utilizado
    override fun onMapReady(googleMap: GoogleMap) {
        // Crear un objeto LatLng con las coordenadas obtenidas
        val ubicacion = LatLng(latitud, longitud)

        // Agregar un marcador en la ubicación guardada con un título
        googleMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación Guardada"))

        // Mover la cámara del mapa a la ubicación seleccionada con un zoom de 15
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
    }
}
