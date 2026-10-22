package com.setometro.app.data.model

data class CotoData(
    val nombre: String,
    val provincia: String,
    val tipoBosque: String,
    val latitud: Double,
    val longitud: Double,
    val altitud: Double,
    val descripcion: String,
    val caracteristicas: String,
    val fuenteCoordenadas: String = "MAP"
)


