package com.example.ejemplo_01

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
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
        R.mipmap.spain_foreground,
        R.mipmap.colombia_foreground,
        R.mipmap.alemania_foreground,
        R.mipmap.italia_foreground,
        R.mipmap.eeuu_foreground
    )
    private var paisSelec = ""

    // Lista muctable que ira incluyendo o eliminando elementos en funcion de las elecciones en los
        // checbox
    // String para mostar los datos
    var hobbies = ""
    var misHobbies = mutableListOf<String>()

    // String que guardara la seleccion del usuario sobre el boletin
    private lateinit var switchBoletin: Switch
    var boletin = ""

    // Instanciamos los checkbox, ya que accederemos a ellos desde un metodo fuera del onCreate()
    private lateinit var checkBoxDeportes: CheckBox
    private lateinit var checkBoxArte: CheckBox
    private lateinit var checkBoxMusica: CheckBox
    private lateinit var checkBoxLectura: CheckBox

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
        val textViewSatis = findViewById<TextView>(R.id.textViewReal)
        var satis = ""
        // Configura los valores mínimo y máximo de la SeekBar
        seekbarSatis.min = 0
        seekbarSatis.max = 10
        seekbarSatis.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            /**
             * Método que se llama cada vez que cambio el progreso
             * seekBar contiene la referencia a la seekBar
             * progress contiene el valor actual de la seekbar
             * fromUser indica si el cambio lo ha producido el usuario al arastar la barra
             */
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Actualiza el TextView mientras se mueve la SeekBar
                textViewSatis.text = "$progress"
                satis = progress.toString()
            }

            /**
             * Se llama cuando el usuario comienza a tocar la seekbar
             */
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            /**
             * Se llama cuando el usuario deja de tocarla
             */
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // ChexBox de los hobbies.
        checkBoxDeportes = findViewById(R.id.checkBoxDeporte)
        checkBoxMusica = findViewById(R.id.checkBoxMusica)
        checkBoxArte = findViewById(R.id.checkBoxArte)
        checkBoxLectura = findViewById(R.id.checkBoxLectura)
        // Metodo que verifica los checkbox seleccionados
        configurarCheckBox()

        // RadioGroup del sexo
        var radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        // Spinner sobre la seleccion del pais, ademas de pasarle un adaptador personalizado
        val selectorCiudades = findViewById<Spinner>(R.id.spinner)
        val adaptadorPersonalizado = AdaptadorPersonalizado(this, R.layout.fila_spinner, pais)
        selectorCiudades.adapter = adaptadorPersonalizado
        selectorCiudades.onItemSelectedListener = this

        // Switch sobre el boletin
        switchBoletin = findViewById(R.id.switchSuscripcion)
        switchBoletin.textOn = "SI"
        switchBoletin.textOff = "NO"
        configurarSwitch()


        // Boton guardar
        val botonGuardar = findViewById<Button>(R.id.buttonGuardar)
        // TextView donde se mostraran los datos seleccionados
        val textViewDatos = findViewById<TextView>(R.id.textViewDatos)

        botonGuardar.setOnClickListener{
            // Almacenamos los datos de los edittext
            val nombre = edittextNombre.text
            val apellido = edittextape.text
            val mail = edittextmail.text
            // Datos de radioGroup
            val selectedSexoID = radioGroup.checkedRadioButtonId
            val selectedSexo: RadioButton?
            var sexo = ""
            if (selectedSexoID != -1) {
                selectedSexo = findViewById(selectedSexoID)
                sexo = selectedSexo.text.toString()
            }
            imprimirHobbies()
            textViewDatos.text = getString(
                R.string.datos,
                nombre,
                apellido,
                mail,
                sexo,
                paisSelec,
                hobbies,
                satis,
                boletin
            )
        }
    }

    private fun imprimirHobbies(){
        hobbies = ""
        for (hobbie in misHobbies){
            hobbies += "$hobbie"
        }
    }

    /**
     * Configuramos un escuchador para cambiar el valor de la variable
     */
    private fun configurarSwitch(){
        switchBoletin.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                boletin = "SI"
            } else {
                boletin = "NO"
            }
        }
    }

    /**
     * Función para configurar escuchador del checkBox
     */
    private fun configurarCheckBox() {
        // Configura un listener para el CheckBox
        //buttonView, isChecked -> Esto define un bloque de código anónimo que se ejecutará cuando
        // se active el listener. buttonView es una referencia al propio CheckBox, y isChecked es
        // un parámetro que indica si el CheckBox está marcado (true) o desmarcado (false) en el
        // momento del cambio.
        checkBoxDeportes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                misHobbies.add("los deportes, ")
            } else {
                misHobbies.remove("los deportes, ")
            }
        }
        checkBoxMusica.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                misHobbies.add("la musica, ")
            } else {
                misHobbies.remove("la musica, ")
            }
        }
        checkBoxArte.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                misHobbies.add("el arte, ")
            } else {
                misHobbies.remove("el arte, ")
            }
        }
        checkBoxLectura.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                misHobbies.add("la lectura, ")
            } else {
                misHobbies.remove("la lectura, ")
            }
        }
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
            paisSelec = pais[position].toString()

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