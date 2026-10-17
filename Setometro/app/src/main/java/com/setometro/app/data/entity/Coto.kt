package com.setometro.app.data.entity

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "coto")
data class Coto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val provincia: String,
    val tipoBosque: String, // 'Pino', 'Roble/Castaño/Haya', 'Encina/Mixto'
    val latitud: Double,
    val longitud: Double,
    val altitud: Double,
    val descripcion: String,
    val caracteristicas: String,
    val seleccionado: Boolean = false,
    @ColumnInfo(defaultValue = "'MANUAL'")
    val fuenteCoordenadas: String = "MANUAL",
    val precisionCoordenadasMetros: Float? = null
)
