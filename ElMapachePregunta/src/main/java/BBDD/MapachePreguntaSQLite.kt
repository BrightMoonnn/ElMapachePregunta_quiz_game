package BBDD


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.elmapachepregunta.Jugador
import com.example.elmapachepregunta.Pregunta
import com.example.elmapachepregunta.Respuesta
import java.io.FileOutputStream

//Usamos SQLiteOpenHelper para gestionar la conexión a la base de datos SQLite
class MapachePreguntaSQLite (private val ctx : Context):
SQLiteOpenHelper (ctx, DATABASE_NAME, null, DATABASE_VERSION) {

    //Al utilizar una base de datos externa es necesario crear un objeto para realizar la conexión
    companion object {
        private const val DATABASE_NAME = "El_Mapache_Pregunta.db"
        private const val DATABASE_VERSION = 2

        //Creamos una función que copie la base de datos desde assets
        fun copiarBaseDatos(context: Context) {
            val rutabd = context.getDatabasePath(DATABASE_NAME)

            if (!rutabd.exists()) {
                rutabd.parentFile.mkdirs()

                context.assets.open("databases/$DATABASE_NAME").use { input ->
                    FileOutputStream(rutabd).use { output ->
                        input.copyTo(output)
                    }
                }

            }

        }
    }
    //SQLiteOpenHelper es una clase abstracta por lo que nos "obliga" a implementar estas funciones aunque están vacías
    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    //Creamos el CRUD de la clase Jugador

    //Función para crear un nuevo registro en la tabla jugador
    fun crearJugador(jugador: Jugador): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", jugador.nombre)
            put("monedas", jugador.monedas)
            put("id_avatar", jugador.id_avatar)
        }

        val result = db.insert("jugador", null, values)
        return result
    }

    //Función para buscar un jugador  con todos sus campos en la base de datos por su ID
    fun obtenerJugadorPorId(idJugador: Int): Jugador? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_jugador, nombre, monedas, id_avatar FROM jugador WHERE id_jugador = ?",
            arrayOf(idJugador.toString())
        )

        var jugador: Jugador? = null
        if (cursor.moveToFirst()) {
            jugador = Jugador(
                id_jugador = cursor.getInt(0),
                nombre = cursor.getString(1),
                monedas = cursor.getInt(2),
                id_avatar = cursor.getInt(3)
            )
        }

        cursor.close()
        return jugador
    }

    //Función para buscar un jugador con todos sus campos en la base de datos por el nombre
    fun obtenerJugadorPorNombre(nombre: String): Jugador? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_jugador, nombre, monedas, id_avatar FROM jugador WHERE nombre = ?",
            arrayOf(nombre)
        )

        var jugador: Jugador? = null
        if (cursor.moveToFirst()) {
            jugador = Jugador(
                id_jugador = cursor.getInt(0),
                nombre = cursor.getString(1),
                monedas = cursor.getInt(2),
                id_avatar = cursor.getInt(3)
            )
        }

        cursor.close()
        return jugador
    }

    //Función para buscar el nombre de un jugador usando su ID
    fun obtenerNombreDeJugador(idJugador: Int): String {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT nombre FROM jugador WHERE id_jugador = ?",
            arrayOf(idJugador.toString())
        )

        var nombre = ""
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0)
        }

        cursor.close()
        return nombre

    }

    //Función para obtener el avatar que tiene el jugador mediante el campo de ID del jugador
    fun obtenerAvatar(idJugador: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_avatar FROM jugador WHERE id_jugador = ?",
            arrayOf(idJugador.toString())
        )

        var avatar = 0
        if (cursor.moveToFirst()) {
            avatar = cursor.getInt(0)
        }

        cursor.close()
        return avatar
    }

    //Función para obtener las monedas de la tabla de jugador mediante la ID del jugador
    fun obtenerMonedas(idJugador: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT monedas FROM jugador WHERE id_jugador = ?",
            arrayOf(idJugador.toString())
        )

        var monedas = 0
        if (cursor.moveToFirst()) {
            monedas = cursor.getInt(0)
        }

        cursor.close()
        return monedas
    }

    //Función para actualizar el campo monedas de la tabla Jugador mediante el campo ID del jugador
    fun añadirMonedas(idJugador: Int, monedas: Int): Jugador? {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("monedas", monedas)
        }

        db.update("jugador", values, "id_jugador = ?", arrayOf(idJugador.toString()))
        val jugador = obtenerJugadorPorId(idJugador)
        return jugador
    }

    //Creamos el CRUD de la clase Pregunta

    //Función para obtener todos los campos de las preguntas usando su campo dificultad y las almacenamos en una lista
    fun obtenerPreguntasPorDificultad(dificultad: Int): List<Pregunta> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM pregunta WHERE dificultad = ?",
            arrayOf(dificultad.toString())
        )

        val listaPreguntas = mutableListOf<Pregunta>()

        if (cursor.moveToFirst()) {
            do {
                val pregunta = Pregunta(
                    id_pregunta = cursor.getInt(0),
                    enunciado = cursor.getString(1),
                    dificultad = cursor.getInt(2)
                )
                listaPreguntas.add(pregunta)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return listaPreguntas
    }
    //Creamos el CRUD de la clase Respuestas

    //Función para obtener todos los campos de las respuestas y organizarlas en una lista utilizando el campo Id de pregunta
    fun obtenerRespuestas(idPregunta: Int): List<Respuesta> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM respuesta WHERE id_pregunta = ?",
            arrayOf(idPregunta.toString())
        )

        val listaRespuestas = mutableListOf<Respuesta>()

        if (cursor.moveToFirst()) {
            do {
                val respuesta = Respuesta(
                    id_respuesta = cursor.getInt(0),
                    texto = cursor.getString(1),
                    id_pregunta = cursor.getInt(2),
                    es_correcta = cursor.getInt(3)
                )
                listaRespuestas.add(respuesta)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return listaRespuestas
    }

    //Función para obtener todos los campos de las respuestas correctas mediante su campo es_correcta
    fun obtenerRespuestaCorrecta(idPregunta: Int): Respuesta? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM respuesta WHERE id_pregunta = ? AND es_correcta = 1",
            arrayOf(idPregunta.toString())
        )

        var respuestaCorrecta: Respuesta? = null
        if (cursor.moveToFirst()) {
            respuestaCorrecta = Respuesta(
                id_respuesta = cursor.getInt(0),
                texto = cursor.getString(1),
                id_pregunta = cursor.getInt(2),
                es_correcta = cursor.getInt(3)
            )
        }

        cursor.close()
        return respuestaCorrecta
    }

    //Creamos el CRUD de la clase Avatar

    //Función para obtener el precio del avatar mediante el campo ID del avatar
    fun obtenerPrecioAvatar(idAvatar: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT precio FROM avatar WHERE id_avatar = ?",
            arrayOf(idAvatar.toString())
        )

        var precio = 0
        if (cursor.moveToFirst()) {
            precio = cursor.getInt(0)
        }

        cursor.close()
        return precio

    }

    //Creamos el CRUD de la tabla intermedia avatar_jugador

    //Función para obtener el avatar que tiene equipado el jugador mediante el campo ID del jugador
    fun obtenerAvatarEquipado(idJugador: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_avatar FROM jugador WHERE id_jugador = ?",
            arrayOf(idJugador.toString())
        )

        var avatar = -1

        if (cursor.moveToFirst()) {
            avatar = cursor.getInt(0)
        }

        cursor.close()
        return avatar
    }


    //Función para actualizar el campo del avatar equipado del jugador
    fun equiparAvatar(idJugador: Int, avatar: Int): Jugador? {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_avatar", avatar)
        }

        db.update("jugador", values, "id_jugador = ?", arrayOf(idJugador.toString()))
        val jugador = obtenerJugadorPorId(idJugador)
        return jugador
    }

    //Función para obtener la lista de avatares que ha comprado el jugador mediante el campo ID del jugador
    fun obtenerAvataresComprados(idJugador: Int): List<Int> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_avatar FROM avatar_jugador WHERE id_jugador = ?",
            arrayOf(idJugador.toString())
        )

        val listaAvatar = mutableListOf<Int>()
        while (cursor.moveToNext()) {
            listaAvatar.add(cursor.getInt(0))
        }

        cursor.close()
        return listaAvatar
    }

    //Función para actualizar la tabla jugador con el avatar que acababa de comprar, usando el campo ID del jugador
    fun comprarAvatar(idJugador: Int, idAvatar: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id_jugador", idJugador)
            put("id_avatar", idAvatar)
        }
        db.insert("avatar_jugador", null, values)
    }


}

