package com.setometro.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "registros_diarios",
    foreignKeys = [
        ForeignKey(
            entity = Coto::class,
            parentColumns = ["id"],
            childColumns = ["cotoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cotoId", "fecha"], unique = true)]
)
data class RegistroDiario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cotoId: Int,
    val fecha: String, // formato YYYY-MM-DD
    val lluviaMm: Double,
    val temperaturaMedia: Double,
    val humedadRelativa: Double // Humedad relativa en %
)
