package UI

import BBDD.MapachePreguntaSQLite
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elmapachepregunta.R

//Esta Activity sigue el mismo código y funcionamiento que la Activity Juego Difícil
class JuegoActivityFacil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego_facil)


        val basedatos = MapachePreguntaSQLite(this)

        val btnMenu = findViewById<Button>(R.id.botonMenu)
        val btnResponder = findViewById<Button>(R.id.buttonResponder)
        val txtPregunta = findViewById<TextView>(R.id.textPregunta)
        val respuesta1F= findViewById<RadioButton>(R.id.radioRespuesta1F)
        val respuesta2F = findViewById<RadioButton>(R.id.radioRespuesta2F)
        val respuesta3F = findViewById<RadioButton>(R.id.radioRespuesta3F)
        val radioGroupF = findViewById<RadioGroup>(R.id.radioGroupFacil)

        var respuestasAcertadas = 0
        var monedasGanadas = 0

        //La única diferencia es que accede a las preguntas con dificultad fácil
        val preguntas = basedatos.obtenerPreguntasPorDificultad(1).shuffled().take(5)
        var indice = 0

        var sonidoacierto = SoundPool(1, AudioManager.STREAM_MUSIC,1)
        val idAcierto= sonidoacierto.load(this,R.raw.sonidoacierto,1)
        var sonidoerror = SoundPool(1, AudioManager.STREAM_MUSIC,1)
        val idError= sonidoerror.load(this,R.raw.sonidoerror,1)

        txtPregunta.text = preguntas[indice].enunciado

        val respuestas = basedatos.obtenerRespuestas(preguntas[indice].id_pregunta).shuffled()
        respuesta1F.text = respuestas[0].texto
        respuesta2F.text = respuestas[1].texto
        respuesta3F.text = respuestas[2].texto

        btnResponder.setOnClickListener {
            val respuestaCorrecta = basedatos.obtenerRespuestaCorrecta(preguntas[indice].id_pregunta)


            if (respuesta1F.isChecked && respuesta1F.text == respuestaCorrecta?.texto) {
                Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show()
                respuestasAcertadas += 1
                monedasGanadas += 10

                val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
                val idJugador = datosJugador.getLong("id_jugador", -1).toInt()

                val monedasActuales = basedatos.obtenerJugadorPorId(idJugador)?.monedas ?: 0

                val nuevasMonedas = monedasActuales + 10

                basedatos.añadirMonedas(idJugador, nuevasMonedas)

                sonidoacierto.play(idAcierto,1f,1f,1,0,1f)

            }else if (respuesta1F.isChecked && respuesta1F.text != respuestaCorrecta?.texto){
                Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
                sonidoerror.play(idError,1f,1f,1,0,1f)
            }

            if (respuesta2F.isChecked && respuesta2F.text == respuestaCorrecta?.texto) {
                Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show()
                respuestasAcertadas += 1
                monedasGanadas += 10

                val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
                val idJugador = datosJugador.getLong("id_jugador", -1).toInt()

                val monedasActuales = basedatos.obtenerJugadorPorId(idJugador)?.monedas ?: 0

                val nuevasMonedas = monedasActuales + 10

                basedatos.añadirMonedas(idJugador, nuevasMonedas)

                sonidoacierto.play(idAcierto,1f,1f,1,0,1f)

            }else if (respuesta2F.isChecked && respuesta2F.text != respuestaCorrecta?.texto){
                Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
                sonidoerror.play(idError,1f,1f,1,0,1f)
            }

            if (respuesta3F.isChecked && respuesta3F.text == respuestaCorrecta?.texto) {
                Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show()
                respuestasAcertadas += 1
                monedasGanadas += 10

                val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
                val idJugador = datosJugador.getLong("id_jugador", -1).toInt()

                val monedasActuales = basedatos.obtenerJugadorPorId(idJugador)?.monedas ?: 0

                val nuevasMonedas = monedasActuales + 10

                basedatos.añadirMonedas(idJugador, nuevasMonedas)

                sonidoacierto.play(idAcierto,1f,1f,1,0,1f)

            }else if(respuesta3F.isChecked && respuesta3F.text != respuestaCorrecta?.texto){
                Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
                sonidoerror.play(idError,1f,1f,1,0,1f)
            }

            if (!respuesta1F.isChecked && !respuesta2F.isChecked && !respuesta3F.isChecked) {
                Toast.makeText(this, "No has seleccionado ninguna respuesta", Toast.LENGTH_SHORT).show()
            }


            if (indice < preguntas.size - 1) {
               radioGroupF.clearCheck()

                indice++

                txtPregunta.text = preguntas[indice].enunciado

                val respuestas = basedatos.obtenerRespuestas(preguntas[indice].id_pregunta).shuffled()
                respuesta1F.text = respuestas[0].texto
                respuesta2F.text = respuestas[1].texto
                respuesta3F.text = respuestas[2].texto

            } else {
                startActivity(Intent(this, FinaljuegoActivity::class.java).putExtra("respuestasAcertadas", respuestasAcertadas).putExtra("monedasGanadas", monedasGanadas))
            }
        }


        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }


    }

}
