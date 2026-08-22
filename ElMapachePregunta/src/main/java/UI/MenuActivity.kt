package UI

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elmapachepregunta.R

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        //Creamos variables de todos los widget de la interfaz gráfica
        val btnJugar = findViewById<Button>(R.id.botonJugar)
        val btnTienda = findViewById<Button>(R.id.botonTienda)
        val btnSalir = findViewById<Button>(R.id.botonSalir)
        val btnPerfil = findViewById<Button>(R.id.botonPerfil)

        //Botón que nos lleva a la Activity Perfil de Jugador
        btnPerfil.setOnClickListener {
            startActivity(Intent(this, PerfilJugadorActivity::class.java))
        }

        //Botón que nos lleva a la Activity Dificultad
        btnJugar.setOnClickListener {
            val accesoMenu = Intent(this, DificultadActivity::class.java)
            startActivity(accesoMenu)
        }

        //Botón que nos lleva a la Activity Tienda
        btnTienda.setOnClickListener {
            startActivity(Intent(this, TiendaActivity::class.java))
        }

        //Botón que cierra la aplicación
        btnSalir.setOnClickListener {
            finishAffinity()
        }
    }
}