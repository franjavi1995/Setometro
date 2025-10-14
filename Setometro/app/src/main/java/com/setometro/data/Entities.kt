package com.setometro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cotos")
data class Coto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val provincia: String,
    val tipo_bosque: String,
    val latitud: Double,
    val longitud: Double,
    val seleccionado: Boolean = false
)

@Entity(tableName = "registros_diarios")
data class RegistroDiario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coto_id: Int,
    val fecha: String,
    val lluvia_mm: Double,
    val temperatura_media: Double
)
