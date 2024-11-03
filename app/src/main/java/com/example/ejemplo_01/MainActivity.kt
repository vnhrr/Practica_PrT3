package com.example.ejemplo_01

import android.content.Context
import android.graphics.Color
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import android.util.Patterns

// El metodo onCreate es el unico que es obligado su sobreescritura, heredado de AppCompactActiviti
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    // Campos de nombre, apellidos y email fuera del onCreate para acceder a ellos desde otros
        // metodos
    private lateinit var edittextNombre: EditText
    private lateinit var edittextape: EditText
    private lateinit var edittextmail:EditText

    // Defino el array de los paises y sus imagenes
    private val pais = arrayOf(
        getString(R.string.campos_vacios),
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

    // Variable en la que almacenar los datos para cuando se rote el dispositivo
    private var guardaDatos : Bundle? = null

    // Variable en la que almacenar los datos
    private var muestraDatos = ""
    private lateinit var textViewDatos: TextView

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

        // Nombre, apellidos y E-Mail
        edittextNombre = findViewById(R.id.editTextTextNombre)
        edittextape = findViewById(R.id.editTextTextApellidos)
        edittextmail = findViewById(R.id.editTextTextEmailAddress)

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
        switchBoletin.textOn = getString(R.string.si)
        switchBoletin.textOff = getString(R.string.no)
        configurarSwitch()


        // Boton guardar
        val botonGuardar = findViewById<Button>(R.id.buttonGuardar)
        // TextView donde se mostraran los datos seleccionados
        textViewDatos = findViewById(R.id.textViewDatos)
        // Comprobamos que el metodo de guardado de la instancia este vacio, si no lo esta
            // restaura el valor del textView que tenia antes de girar el dispositivo
        if (savedInstanceState != null){
            muestraDatos = savedInstanceState.getString("datos", "")
            textViewDatos.text = muestraDatos
        }

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
            if ((nombre.toString().equals("")) or (apellido.toString().equals("")) or
                (mail.toString().equals(""))){
                comprobarVacios(nombre.toString(), getString(R.string.nombre))
                comprobarVacios(apellido.toString(), getString(R.string.aellidos))
                comprobarVacios(mail.toString(), getString(R.string.e_mail))
                textViewDatos.text = getString(R.string.campos_vacios)
                textViewDatos.setTextColor(Color.RED)
            }
            else if (!validarMail(mail.toString())){
                Toast.makeText(this, getString(R.string.formato_mail),
                    Toast.LENGTH_SHORT).show()
                textViewDatos.text = getString(R.string.formato_mail)
                textViewDatos.setTextColor(Color.RED)

            } else {
                textViewDatos.setTextColor(Color.BLACK)
                imprimirHobbies()
                muestraDatos = getString(
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
                guardaDatos?.putString("datos", muestraDatos)
                textViewDatos.text = muestraDatos.toString()
            }
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
                boletin = getString(R.string.si)
            } else {
                boletin = getString(R.string.no)
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
                misHobbies.add(getString(R.string.deporte))
            } else {
                misHobbies.remove(getString(R.string.deporte))
            }
        }
        checkBoxMusica.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                misHobbies.add(getString(R.string.musica))
            } else {
                misHobbies.remove(getString(R.string.musica))
            }
        }
        checkBoxArte.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                misHobbies.add(getString(R.string.arte))
            } else {
                misHobbies.remove(getString(R.string.arte))
            }
        }
        checkBoxLectura.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                misHobbies.add(getString(R.string.lectura))
            } else {
                misHobbies.remove(getString(R.string.lectura))
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

    // Metodo que se usa para guardar la instancia, en este caso del texto para que se siga mostrando
        // a esar de girar la pantalla
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("datos", muestraDatos)
        super.onSaveInstanceState(outState)
    }

    /**
     * Compobamos que los campos obligatorios esten rellenados
     */
    fun comprobarVacios(texto: String, campo: String){
        if (texto.toString().equals("")){
            Toast.makeText(this, "El campo $campo no puede estar vacio",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun validarMail(mail:String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(mail.toString()).matches()
    }
}