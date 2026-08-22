package UI

import BBDD.MapachePreguntaSQLite
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elmapachepregunta.R

class PerfilJugadorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_jugador)

        //Creamos una variable con un objeto de SQLiteOpenHelper para poder acceder a la base de datos
        val basedatos = MapachePreguntaSQLite(this)

        //Recogemos los datos del jugador mediante Shared References
        val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
        val idJugador = datosJugador.getLong("id_jugador", -1).toInt()

        //Obtenemos de la base de datos los avatares que tiene el jugador
        val avataresComprados = basedatos.obtenerAvataresComprados(idJugador)

        //Creamos variables de todos los widget de la interfaz gráfica
        val btnMenu = findViewById<Button>(R.id.botonMenu)
        val btnEquiparAvatar = findViewById<Button>(R.id.buttonEquiparAvatar)
        val rbGato = findViewById<RadioButton>(R.id.radioButtonGato)
        val rbPerro = findViewById<RadioButton>(R.id.radioButtonPerro)
        val rbPanda = findViewById<RadioButton>(R.id.radioButtonPanda)
        val rbHamster = findViewById<RadioButton>(R.id.radioButtonHamster)
        val rbMapache = findViewById<RadioButton>(R.id.radioButtonMapache)
        val rbMapacheDorado = findViewById<RadioButton>(R.id.radioButtonMapacheDorado)

        //Si el jugador tiene el avatar en su inventario el radio button aparece activado
        rbGato.isEnabled = avataresComprados.contains(1)
        rbPerro.isEnabled = avataresComprados.contains(2)
        rbPanda.isEnabled = avataresComprados.contains(3)
        rbHamster.isEnabled = avataresComprados.contains(4)
        rbMapache.isEnabled = avataresComprados.contains(5)
        rbMapacheDorado.isEnabled = avataresComprados.contains(6)

        //Este botón nos devuelve a la Activity Menu
        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        //Código que se ejecuta al presionar el botón Equipar
        btnEquiparAvatar.setOnClickListener {
            //Creamos una variable que recoja los avatares que tiene actualmente el jugador en su inventario
            val listaAvataresActualizada = basedatos.obtenerAvataresComprados(idJugador)

            //Si se selecciona un avatar y está en el inventario del jugador, se actualizará la base de datos con ese avatar equipado y el radio button aparecerá desactivado
            if (rbGato.isChecked && listaAvataresActualizada.contains(1)) {
                basedatos.equiparAvatar(idJugador, 1)
                rbGato.isEnabled = false
                Toast.makeText(this, "Avatar Gato Equipado", Toast.LENGTH_SHORT).show()

            } else if (rbPerro.isChecked && listaAvataresActualizada.contains(2)) {
                basedatos.equiparAvatar(idJugador, 2)
                rbPerro.isEnabled = false
                Toast.makeText(this, "Avatar Perro Equipado", Toast.LENGTH_SHORT).show()

            } else if (rbPanda.isChecked && listaAvataresActualizada.contains(3)) {
                basedatos.equiparAvatar(idJugador, 3)
                rbPanda.isEnabled = false
                Toast.makeText(this, "Avatar Panda Equipado", Toast.LENGTH_SHORT).show()

            } else if (rbHamster.isChecked && listaAvataresActualizada.contains(4)) {
                basedatos.equiparAvatar(idJugador, 4)
                rbHamster.isEnabled = false
                Toast.makeText(this, "Avatar Hamster Equipado", Toast.LENGTH_SHORT).show()

            } else if (rbMapache.isChecked && listaAvataresActualizada.contains(5)) {
                basedatos.equiparAvatar(idJugador, 5)
                rbMapache.isEnabled = false
                Toast.makeText(this, "Avatar Mapache Equipado", Toast.LENGTH_SHORT).show()

            } else if (rbMapacheDorado.isChecked && listaAvataresActualizada.contains(6)) {
                basedatos.equiparAvatar(idJugador, 6)
                rbMapacheDorado.isEnabled = false
                Toast.makeText(this, "Avatar Mapache Dorado Equipado", Toast.LENGTH_SHORT).show()
            }


        }


    }

}