import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.deber_02db.R
import com.example.deber_02db.model.Parte
import java.text.SimpleDateFormat
import java.util.Locale

class ParteListAdapter(context: Context, partes: List<Parte>) :
    ArrayAdapter<Parte>(context, 0, partes) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_lista_auto, parent, false)
        val parte = getItem(position)


        // Referencias a los elementos del layout reutilizado
        val tvNombreAuto = view.findViewById<TextView>(R.id.tvNombreAuto)
        val tvMarcaAuto = view.findViewById<TextView>(R.id.tvMarcaAuto)
        val ivAuto = view.findViewById<ImageView>(R.id.ivAuto)
        val tvFechaFabricacion = view.findViewById<TextView>(R.id.tvAnio)


        // Configurar los valores para las partes
        tvNombreAuto.text = parte?.nombreParte
        tvMarcaAuto.text = "Precio: ${parte?.precio}"
        tvFechaFabricacion.text = "Fecha de Reemplazo: ${dateFormat.format(parte?.fechaReemplazo)}"


        // Cambiar la imagen si es necesario (o usar una imagen predeterminada)
        ivAuto.setImageResource(R.drawable.ic_default_car)  // Puedes cambiarlo si tienes imágenes específicas

        return view
    }
}
