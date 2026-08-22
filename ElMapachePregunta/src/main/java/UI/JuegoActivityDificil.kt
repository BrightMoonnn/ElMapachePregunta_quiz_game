package UI

import BBDD.MapachePreguntaSQLite
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elmapachepregunta.R

class JuegoActivityDificil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego_dificil)

        //Creamos una variable con un objeto de SQLiteOpenHelper para poder acceder a la base de datos
        val basedatos = MapachePreguntaSQLite(this)

        //Creamos variables de todos los widget de la interfaz gráfica
        val btnMenu = findViewById<Button>(R.id.botonMenu)
        val btnResponder = findViewById<Button>(R.id.buttonResponder)
        val txtPregunta = findViewById<TextView>(R.id.textPregunta)
        val respuesta1D= findViewById<RadioButton>(R.id.radioRespuesta1D)
        val respuesta2D = findViewById<RadioButton>(R.id.radioRespuesta2D)
        val respuesta3D = findViewById<RadioButton>(R.id.radioRespuesta3D)
        val radioGroupD = findViewById<RadioGroup>(R.id.radioGroupDificil)

        //Creamos dos variables que recojan el número de respuestas acertadas y monedas ganadas inicializándolos a 0
        var respuestasAcertadas = 0
        var monedasGanadas = 0

        //Accedemos a la base de datos para obtener las preguntas con dificultad Difícil, mezcladas y obteniendo 5
        val preguntas = basedatos.obtenerPreguntasPorDificultad(2).shuffled().take(5)

        //Creamos un índice que irá contando cada vez que se realiza una pregunta y lo inicializamos a 0
        var indice = 0

        //Creamos variables con los sonidos que queremos en nuestra aplicación
        var sonidoacierto = SoundPool(1, AudioManager.STREAM_MUSIC,1)
        val idAcierto = sonidoacierto.load(this,R.raw.sonidoacierto,1)
        var sonidoerror = SoundPool(1, AudioManager.STREAM_MUSIC,1)
        val idError = sonidoerror.load(this,R.raw.sonidoerror,1)


        //En este textView aparece el enunciado de las preguntas
        txtPregunta.text = preguntas[indice].enunciado

        //Accedemos a la base de datos para obtener las respuestas correspondientes a cada pregunta, saldrán mezcladas
        val respuestas = basedatos.obtenerRespuestas(preguntas[indice].id_pregunta).shuffled()

        //En cada uno de los radio button aparece el texto de cada una de las respuestas
        respuesta1D.text = respuestas[0].texto
        respuesta2D.text = respuestas[1].texto
        respuesta3D.text = respuestas[2].texto


        //Este botón realiza la comprobación de las respuestas y el paso a la siguiente Activity una vez que se ha acabado la ronda de preguntas
        btnResponder.setOnClickListener {
            //Accedemos a la base de datos para obtener la respuesta correcta de cada pregunta
            val respuestaCorrecta = basedatos.obtenerRespuestaCorrecta(preguntas[indice].id_pregunta)

            //Si se ha elegido la respuesta 1 y es correcta se ejecuta este código
            if (respuesta1D.isChecked && respuesta1D.text == respuestaCorrecta?.texto) {
                Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show()
                respuestasAcertadas += 1
                monedasGanadas += 20

                //Obtenemos los datos del jugador provenientes de SharedReferences en modo privado para que solo la aplicación acceda a ellos
                val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
                //Obtenemos el ID de Jugador de esos datos u obtenemos -1 si no existe un jugador
                val idJugador = datosJugador.getLong("id_jugador", -1).toInt()

                //Accedemos a la base de datos para obtener las monedas del jugador
                val monedasActuales = basedatos.obtenerJugadorPorId(idJugador)?.monedas ?: 0

                //Sumamos las monedas ganadas en esta pregunta a las monedas que ya tiene el jugador actualmente
                val nuevasMonedas = monedasActuales + 20

                //Actualizamos el campo monedas de la tabla jugador con las monedas ya actualizadas
                basedatos.añadirMonedas(idJugador, nuevasMonedas)

                //Suena el sonido elegido para acierto
                sonidoacierto.play(idAcierto,1f,1f,1,0,1f)

            //Si la repuesta no es correcta saldrá este mensaje y se pasa a la siguiente pregunta
            }else if (respuesta1D.isChecked && respuesta1D.text != respuestaCorrecta?.texto){
                Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()

                //Se ejecuta el sonido elegido para error
                sonidoerror.play(idError,1f,1f,1,0,1f)
            }

            //Se realiza el mismo procedimiento con las otras dos respuestas
            if (respuesta2D.isChecked && respuesta2D.text == respuestaCorrecta?.texto) {
                Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show()
                respuestasAcertadas += 1
                monedasGanadas += 20

                val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
                val idJugador = datosJugador.getLong("id_jugador", -1).toInt()

                val monedasActuales = basedatos.obtenerJugadorPorId(idJugador)?.monedas ?: 0

                val nuevasMonedas = monedasActuales + 20

                basedatos.añadirMonedas(idJugador, nuevasMonedas)
                sonidoacierto.play(idAcierto,1f,1f,1,0,1f)


            }else if (respuesta2D.isChecked && respuesta2D.text != respuestaCorrecta?.texto){
                Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
                sonidoerror.play(idError,1f,1f,1,0,1f)
            }

            if (respuesta3D.isChecked && respuesta3D.text == respuestaCorrecta?.texto) {
                Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show()
                respuestasAcertadas += 1
                monedasGanadas += 20

                val datosJugador = getSharedPreferences("datos", MODE_PRIVATE)
                val idJugador = datosJugador.getLong("id_jugador", -1).toInt()

                val monedasActuales = basedatos.obtenerJugadorPorId(idJugador)?.monedas ?: 0

                val nuevasMonedas = monedasActuales + 20

                basedatos.añadirMonedas(idJugador, nuevasMonedas)

                sonidoacierto.play(idAcierto,1f,1f,1,0,1f)

            }else if(respuesta3D.isChecked && respuesta3D.text != respuestaCorrecta?.texto){
                Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
                sonidoerror.play(idError,1f,1f,1,0,1f)
            }

            //Si no se ha seleccionado ninguna respuesta aparecerá este mensaje y se cargará la siguiente pregunta
            if (!respuesta1D.isChecked && !respuesta2D.isChecked && !respuesta3D.isChecked) {
                Toast.makeText(this, "No has seleccionado ninguna respuesta", Toast.LENGTH_SHORT).show()
            }

            //si el indice de preguntas es menor al tamaño de la lista de las mismas se realizarán las siguientes instrucciones
            if (indice < preguntas.size - 1) {
                //Se resetearán los radiobutton para que no aparezca ninguno seleccionado en la nueva pregunta
                radioGroupD.clearCheck()

                //Avanzamos a la siguiente pregunta y sumamos uno al índice
                indice++

                //Aparece en este textView el enunciado de la siguiente pregunta según el índice
                txtPregunta.text = preguntas[indice].enunciado

                //Accedemos a la base de datos para obtener las respuestas de cada pregunta según el índice, irán mezcladas para que no aparezcan siempre en el mismo orden
                val respuestas = basedatos.obtenerRespuestas(preguntas[indice].id_pregunta).shuffled()

                //Asignamos el texto de cada pregunta a cada radio button
                respuesta1D.text = respuestas[0].texto
                respuesta2D.text = respuestas[1].texto
                respuesta3D.text = respuestas[2].texto

            //cuando el índice es mayor que el tamaño de la lista de preguntas se pasa a la siguiente Activity
            } else {
                startActivity(Intent(this, FinaljuegoActivity::class.java).putExtra("respuestasAcertadas", respuestasAcertadas).putExtra("monedasGanadas", monedasGanadas))
            }
        }

        //Este botón nos devuelve a la Activity Menu
        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }


    }

}
