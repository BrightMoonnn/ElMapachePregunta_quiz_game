package UI

import BBDD.MapachePreguntaSQLite
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elmapachepregunta.R

class TiendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tienda)

        //Creamos una variable con un objeto de SQLiteOpenHelper para poder acceder a la base de datos
        val basedatos = MapachePreguntaSQLite(this)

        //Creamos variables de todos los widget de la interfaz gráfica
        val btnMenu = findViewById<Button>(R.id.botonMenu)
        val btnComprar = findViewById<Button>(R.id.botonComprar)
        val rbPerro = findViewById<RadioButton>(R.id.radioButtonPerro)
        val rbPanda = findViewById<RadioButton>(R.id.radioButtonPanda)
        val rbGato = findViewById<RadioButton>(R.id.radioButtonGato)
        val rbHamster = findViewById<RadioButton>(R.id.radioButtonHamster)
        val rbMapache = findViewById<RadioButton>(R.id.radioButtonMapache)
        val rbMapacheDorado = findViewById<RadioButton>(R.id.radioButtonMapacheDorado)
        val textNombreMonedas = findViewById<TextView>(R.id.textNombreMonedas)

        //Recogemos los datos del jugador mediante Shared References
        val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
        val idJugador = datosJugador.getLong("id_jugador", -1).toInt()

        //Obtenemos de la base de datos las monedas del jugador, el precio de cada avatar, el nombre del jugador y los avatares que ya tiene en su inventario
        val monedasJugador = basedatos.obtenerMonedas(idJugador)
        val precioAvatarGato = basedatos.obtenerPrecioAvatar(1)
        val precioAvatarPerro = basedatos.obtenerPrecioAvatar(2)
        val precioAvatarPanda = basedatos.obtenerPrecioAvatar(3)
        val precioAvatarHamster = basedatos.obtenerPrecioAvatar(4)
        val precioAvatarMapache = basedatos.obtenerPrecioAvatar(5)
        val precioAvatarMapacheDorado = basedatos.obtenerPrecioAvatar (6)
        val nombreJugador = basedatos.obtenerNombreDeJugador(idJugador)
        val avataresComprados = basedatos.obtenerAvataresComprados(idJugador)

        //El nombre del jugador y las monedas que tiene aparecerán en este TextView
        textNombreMonedas.text = "Hola $nombreJugador, tienes $monedasJugador monedas"

        //Los radio button estarán activos si el jugador no tiene en su inventario ese avatar
        rbPerro.isEnabled = !avataresComprados.contains(2)
        rbPanda.isEnabled = !avataresComprados.contains(3)
        rbGato.isEnabled = !avataresComprados.contains(1)
        rbHamster.isEnabled = !avataresComprados.contains(4)
        rbMapache.isEnabled = !avataresComprados.contains(5)
        rbMapacheDorado.isEnabled = !avataresComprados.contains(6)

        //Este botón nos devuelve a la Activity Menu
        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java).putExtra("idJugador", idJugador).putExtra("monedasJugador", monedasJugador).putExtra("avatarJugador", basedatos.obtenerAvatarEquipado(idJugador)))
        }

        //Al presionar el botón Comprar se ejecutará este código
        btnComprar.setOnClickListener {

            //Si se selecciona un radio button de un avatar y el jugador tiene más monedas o igual al precio del avatar, se lanzará ese mensaje y se actualizará la base de datos con el nuevo avatar comprado y las monedas restantes
            if (rbPerro.isChecked && monedasJugador >= precioAvatarPerro) {
                basedatos.comprarAvatar(idJugador, 2)
                basedatos.añadirMonedas(idJugador, monedasJugador - precioAvatarPerro)
                Toast.makeText(this, "Avatar Perro comprado", Toast.LENGTH_SHORT).show()

            } else if (rbPanda.isChecked && monedasJugador >= precioAvatarPanda) {
                basedatos.comprarAvatar(idJugador, 3)
                basedatos.añadirMonedas(idJugador, monedasJugador - precioAvatarPanda)
                Toast.makeText(this, "Avatar Panda comprado", Toast.LENGTH_SHORT).show()

            } else if (rbGato.isChecked && monedasJugador >= precioAvatarGato) {
                basedatos.comprarAvatar(idJugador, 1)
                basedatos.añadirMonedas(idJugador, monedasJugador - precioAvatarGato)
                Toast.makeText(this, "Avatar Gato comprado", Toast.LENGTH_SHORT).show()

            } else if (rbHamster.isChecked && monedasJugador >= precioAvatarHamster) {
                basedatos.comprarAvatar(idJugador, 4)
                basedatos.añadirMonedas(idJugador, monedasJugador - precioAvatarHamster)
                Toast.makeText(this, "Avatar Hamster comprado", Toast.LENGTH_SHORT).show()

            } else if (rbMapache.isChecked && monedasJugador >= precioAvatarMapache) {
                basedatos.comprarAvatar(idJugador, 5)
                basedatos.añadirMonedas(idJugador, monedasJugador - precioAvatarMapache)
                Toast.makeText(this, "Avatar Mapache comprado", Toast.LENGTH_SHORT).show()

            } else if (rbMapacheDorado.isChecked && monedasJugador >= precioAvatarMapacheDorado) {
                basedatos.comprarAvatar(idJugador, 6)
                basedatos.añadirMonedas(idJugador, monedasJugador - precioAvatarMapacheDorado)
                Toast.makeText(this, "Avatar Mapache Dorado comprado", Toast.LENGTH_SHORT).show()

            //Si no se ha seleccionado ningún radio button, se lanzará este mensaje
            }else if (!rbPerro.isChecked && !rbPanda.isChecked && !rbGato.isChecked && !rbHamster.isChecked && !rbMapache.isChecked && !rbMapacheDorado.isChecked) {
                Toast.makeText(this, "No has seleccionado ningún avatar", Toast.LENGTH_SHORT).show()

            //Si no se cumple ninguna de las otras condiciones se lanzará este mensaje
            }else{
                Toast.makeText(this, "No tienes suficientes monedas", Toast.LENGTH_SHORT).show()
            }

           //Se actualiza la base de datos con los nuevos avatares comprados
            val avataresCompradosActualizados = basedatos.obtenerAvataresComprados(idJugador)

            //Se vuelve a comprobar que avatares están ya en el inventario del jugador para desactivar los radio button de los mismos
            rbPerro.isEnabled = !avataresCompradosActualizados.contains(2)
            rbPanda.isEnabled = !avataresCompradosActualizados.contains(3)
            rbGato.isEnabled = !avataresCompradosActualizados.contains(1)
            rbHamster.isEnabled = !avataresComprados.contains(4)
            rbMapache.isEnabled = !avataresComprados.contains(5)
            rbMapacheDorado.isEnabled = !avataresComprados.contains(6)
            //Se recarga la página con los datos actualizados
            startActivity(Intent(this, TiendaActivity::class.java).putExtra("idJugador", idJugador).putExtra("monedasJugador", basedatos.obtenerMonedas(idJugador)).putExtra("avatarJugador", basedatos.obtenerAvatarEquipado(idJugador)))
        }







    }
}