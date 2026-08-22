package com.example.elmapachepregunta

data class Respuesta(
    var id_respuesta: Int,
    var id_pregunta: Int,
    var texto: String,
    var es_correcta: Int

)