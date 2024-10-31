package com.example.ejemplo_01

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// El metodo onCreate es el unico que es obligado su sobreescritura, heredado de AppCompactActiviti
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    // Defino el array de los paises y sus imagenes
    private val pais = arrayOf(
        "España",
        "Colombia",
        "Alemania",
        "Italia",
        "EEUU"
    )
    private val paisImg = intArrayOf(
        R.drawable.spain_background,
        R.drawable.colombia_background,
        R.drawable.alemania_background,
        R.drawable.italia_background,
        R.drawable.eeuu_background
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instancio los elementos graficos

        // Campos de nombre, apellidos y email
        val edittextNombre = findViewById<EditText>(R.id.editTextTextNombre)
        val edittextape = findViewById<EditText>(R.id.editTextTextApellidos)
        val edittextmail = findViewById<EditText>(R.id.editTextTextEmailAddress)

        // Seekbar de la satisfaccion y su textView que muestre el valor seleccionado
        val seekbarSatis = findViewById<SeekBar>(R.id.seekBarSatisfaccion)
        val textViewSatis = findViewById<TextView>(R.id.textViewSatisfaccion)
        var Satis = ""
        // Configura los valores mínimo y máximo de la SeekBar
        seekbarSatis.min = 0
        seekbarSatis.max = 10

        // RadioGroup del sexo
        var radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        // Spinner sobre la seleccion del pais, ademas de pasarle un adaptador personalizado
        val selectorCiudades = findViewById<Spinner>(R.id.spinner)
        val adaptadorPersonalizado = AdaptadorPersonalizado(this, R.layout.fila_spinner, pais)
        selectorCiudades.adapter = adaptadorPersonalizado
        selectorCiudades.onItemSelectedListener = this
    }
    /**
     * Creo una subclase de array adapter para personalizar el adaptador
     */
    private inner class AdaptadorPersonalizado(
        context: Context,
        resource: Int,
        objects: Array<String>
    ) : ArrayAdapter<String>(context, resource, objects) {
        //Constructor de mi adaptador paso el contexto (this)
        // el layout, y los elementos

        /**
         * Reescribo el método getDropDownView para que me devuelva una fila personalizada en la
         * lista desplegable en vez del elemento que se encuentra en esa posición
         * @param posicion
         * @param ViewConvertida
         * @param padre
         * @return
         */
        override fun getDropDownView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            // Llama a la función para crear la fila personalizada y la devuelve
            return crearFilaPersonalizada(position, convertView, parent)
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            // Este método se llama para mostrar una vista personalizada en el elemento seleccionado

            // Llama a la función para crear la fila personalizada y la devuelve
            return crearFilaPersonalizada(position, convertView, parent)
        }

        /**
         * Método que me crea mis filas personalizadas pasando como parámetro la posición
         * la vista y la vista padre
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        private fun crearFilaPersonalizada(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {

            // Crea un objeto LayoutInflater para inflar la vista personalizada desde un diseño XML
            val layoutInflater = LayoutInflater.from(context)

            //Declaro una vista de mi fila, y la preparo para inflarla con datos
            // Los parametros son: XML descriptivo
            // Vista padre
            // Booleano que indica si se debe ceñir a las características del padre
            val rowView = convertView ?: layoutInflater.inflate(R.layout.fila_spinner, parent, false)

            //Fijamos el nombre del pais
            rowView.findViewById<TextView>(R.id.textViewNombrePais).text = pais[position]

            //Fijamos la imagen de la bandera
            rowView.findViewById<ImageView>(R.id.imageViewBanderaPais).setImageResource(paisImg[position])

            // Devuelve la vista de fila personalizada
            return rowView
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val c = view?.findViewById<TextView>(R.id.textViewNombrePais)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}