package UI

import BBDD.MapachePreguntaSQLite
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elmapachepregunta.Jugador
import com.example.elmapachepregunta.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Hacemos una copia de la base de datos en esta Activity
        MapachePreguntaSQLite.copiarBaseDatos(this)
        //Creamos una variable con un objeto de SQLiteOpenHelper para poder acceder a la base de datos
        val basedatos = MapachePreguntaSQLite(this)

        //Creamos variables de todos los widget de la interfaz gráfica
        val btnInicio = findViewById<Button>(R.id.botonInicio)
        val btnSalida = findViewById<Button>(R.id.botonSalida)
        val cajaNombre = findViewById<TextView>(R.id.editTextNombre)

        //Creamos una función para guardar la id del jugador mediante SharedReferences
        fun guardarDatosJugador(id: Int) {
            val guardarDatos = getSharedPreferences("datos", MODE_PRIVATE)
            guardarDatos.edit()
                .putLong("id_jugador", id.toLong())
                .commit()
        }

        //Código que se ejecuta al presionar el botón Inicio
        btnInicio.setOnClickListener {
            //Creamos una variable que recoja el nombre introducido por el usuario
            val nombre = cajaNombre.text.toString().trim()
            //Si no se introduce  ningún nombre se lanza este mensaje
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Introduce un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Buscamos en la base de datos si ese jugador ya existe
            val jugadorExistente = basedatos.obtenerJugadorPorNombre(nombre)

            //Si el jugador existe se cargan sus datos desde la base de datos y pasamos a la Activity Menu
            if (jugadorExistente != null) {
                guardarDatosJugador(
                    jugadorExistente.id_jugador)

                startActivity(Intent(this, MenuActivity::class.java))
                finish()
                return@setOnClickListener
            }

            //Si el jugador no existe se crea un registro en la base de datos con el nuevo jugador y pasamos al Activity Menu
            val nuevoJugador = Jugador(nombre = nombre, monedas = 0, id_avatar = 1)
            val jugadorCreado = basedatos.crearJugador(nuevoJugador)
            guardarDatosJugador(jugadorCreado.toInt())

            startActivity(Intent(this, MenuActivity::class.java))
        }

        //Botón que hace que la aplicación se cierre
        btnSalida.setOnClickListener {
            finishAffinity()
        }
    }


}