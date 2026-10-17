package com.setometro.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "especies_seta")
data class EspecieSeta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val nombreCientifico: String,
    val descripcion: String,
    val temporadaInicio: String,
    val temporadaFin: String,
    val lluviaMinima: Double, // mm en 15 días
    val lluviaMaxima: Double, // mm en 15 días
    val temperaturaMinima: Double, // °C media en 15 días
    val temperaturaMaxima: Double, // °C media en 15 días
    val altitudMinima: Double, // metros
    val altitudMaxima: Double, // metros
    val tiposBosqueCompatibles: String, // "Pino,Roble/Castaño/Haya"
    val activa: Boolean = true // Si el usuario quiere monitorear esta especie
)




