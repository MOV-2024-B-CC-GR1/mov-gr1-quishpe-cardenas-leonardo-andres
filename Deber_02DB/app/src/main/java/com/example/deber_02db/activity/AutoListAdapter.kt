package com.example.deber_02db.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.deber_02db.model.Auto
import com.example.deber_02db.R
import java.text.SimpleDateFormat
import java.util.*

class AutoListAdapter(context: Context, autos: List<Auto>) :
    ArrayAdapter<Auto>(context, 0, autos) {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Inflar la vista personalizada
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_lista_auto, parent, false)
        val auto = getItem(position)

        // Referencias a los elementos del layout
        val tvNombreAuto = view.findViewById<TextView>(R.id.tvNombreAuto)
        val tvMarcaAuto = view.findViewById<TextView>(R.id.tvMarcaAuto)
        val tvAnio = view.findViewById<TextView>(R.id.tvAnio)
        val ivAuto = view.findViewById<ImageView>(R.id.ivAuto)

        // Configurar los valores del auto
        tvNombreAuto.text = auto?.nombre
        tvMarcaAuto.text = "Marca: ${auto?.marca}"
        tvAnio.text = "Año: ${auto?.fechaFabricacion?.let { yearFormat.format(it) }}"

        // Configurar la imagen dependiendo de la marca
        when (auto?.marca?.lowercase()) {
            "toyota" -> ivAuto.setImageResource(R.drawable.ic_toyota)
            "ford" -> ivAuto.setImageResource(R.drawable.ic_ford)
            "bmw" -> ivAuto.setImageResource(R.drawable.ic_bmw)
            "chevrolet" -> ivAuto.setImageResource(R.drawable.ic_chevrolet)
            "kia" -> ivAuto.setImageResource(R.drawable.ic_kia)
            "honda" -> ivAuto.setImageResource(R.drawable.ic_honda)
            "mazda" -> ivAuto.setImageResource(R.drawable.ic_mazda)
            else -> ivAuto.setImageResource(R.drawable.ic_default_car)
        }

        return view
    }
}
