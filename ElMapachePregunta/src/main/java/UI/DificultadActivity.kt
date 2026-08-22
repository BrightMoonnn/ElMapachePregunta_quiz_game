package UI

import BBDD.MapachePreguntaSQLite
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elmapachepregunta.R

class DificultadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dificultad)

        //Creamos una variable con un objeto de SQLiteOpenHelper para poder acceder a la base de datos
        val basedatos = MapachePreguntaSQLite(this)

        //Creamos variables de todos los widget de la interfaz gráfica
        val btnMenu = findViewById<Button>(R.id.botonMenu)
        val btnEmpezar = findViewById<Button>(R.id.buttonEmpezar)
        val rbFacil = findViewById<RadioButton>(R.id.radioButtonFacil)
        val rbDificil = findViewById<RadioButton>(R.id.radioButtonDificil)
        val txtNombre = findViewById<TextView>(R.id.textnombre)
        val txtMonedas = findViewById<TextView>(R.id.textmonedas)
        val imgAvatar = findViewById<ImageView>(R.id.ImgAvatar)


        //El Botón Empezar solo se activa si se selecciona uno de los radio button
        rbFacil.setOnClickListener { btnEmpezar.isEnabled = true }
        rbDificil.setOnClickListener { btnEmpezar.isEnabled = true }

        //Condicional que indica a que Activity se debe acceder al pulsar el botón Empezar dependiendo del radio button elegido
        btnEmpezar.setOnClickListener {
            if (rbFacil.isChecked) {
                startActivity(Intent(this, JuegoActivityFacil::class.java))
            } else if (rbDificil.isChecked) {
                startActivity(Intent(this, JuegoActivityDificil::class.java))
                //Si no se elige ningún radio button sale este mensaje y no se avanza a otra Activity
            } else {
                Toast.makeText(this, "Selecciona una dificultad", Toast.LENGTH_SHORT).show()
            }
        }


        //Este botón nos devuelve a la Activity Menu
        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        //Obtenemos los datos del jugador provenientes de SharedReferences en modo privado para que solo la aplicación acceda a ellos
        val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
        //Obtenemos el ID de Jugador de esos datos u obtenemos -1 si no existe un jugador
        val idJugador = datosJugador.getLong("id_jugador", -1).toInt()
        //Usamos la Id de jugador obtenida para consultar la base de datos y obtener los datos del jugador
        val jugador = basedatos.obtenerJugadorPorId(idJugador)
        //Guardamos el nombre y monedas del jugador con "" si no hay nombre y con 0 si no hay monedas
        val nombre = jugador?.nombre ?: ""
        val monedas = jugador?.monedas ?: 0

        //En estos TextView aparecerán los datos guardados en nombre y monedas
        txtNombre.text = nombre
        txtMonedas.text = "Monedas: $monedas"

        //Accedemos a la base de datos para obtener el avatar equipado por el jugador
        val avatarJugador = basedatos.obtenerAvatar(idJugador)

        //Asignamos una imagen a cada ID del avatar
        when (avatarJugador) {
            1 -> imgAvatar.setImageResource(R.drawable.gato)
            2 -> imgAvatar.setImageResource(R.drawable.perro)
            3 -> imgAvatar.setImageResource(R.drawable.panda)
            4 -> imgAvatar.setImageResource(R.drawable.hamster)
            5 -> imgAvatar.setImageResource(R.drawable.mapachevatar)
            6 -> imgAvatar.setImageResource(R.drawable.mapachedorado)
            //Si aún no se ha equipado ningún avatar se equipará este por defecto
            else -> imgAvatar.setImageResource(R.drawable.gato)
        }

    }
}
