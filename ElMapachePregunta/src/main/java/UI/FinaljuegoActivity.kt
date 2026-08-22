package UI

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elmapachepregunta.R

class FinaljuegoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finaljuego)

        //Creamos variables de todos los widget de la interfaz gráfica
        val btnMenu = findViewById<Button>(R.id.botonMenu)
        val txtMonedas = findViewById<TextView>(R.id.textoMonedas)
        val txtFinal = findViewById<TextView>(R.id.textoFinal)
        //Recogemos el total de respuestas acertadas desde el Activity Anterior para decidir qué mensaje se mostrará
        val respuestasAcertadas = intent.getIntExtra("respuestasAcertadas", 0)

        //Si el número de respuestas acertadas es 0 se lanza este mensaje
        if (respuestasAcertadas == 0) {
            txtFinal.text = "¡Qué pena!, no has acertado ninguna pregunta"
            txtMonedas.text = "Has ganado 0 monedas"

        //Si se ha acertado 1 o más respuestas se lanzará este mensaje con el total de preguntas acertadas y monedas ganadas, estas úlitmas vienen del Activity anterior
        }else {
            val monedasGanadas = intent.getIntExtra("monedasGanadas", 0)
            txtFinal.text = "Has acertado $respuestasAcertadas preguntas"
            txtMonedas.text = "Has ganado $monedasGanadas monedas"
        }

        //Este botón nos devuelve a la Activity Menu
        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

    }
}
