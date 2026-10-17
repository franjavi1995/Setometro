package com.setometro.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "datos_suelo",
    foreignKeys = [
        ForeignKey(
            entity = Coto::class,
            parentColumns = ["id"],
            childColumns = ["cotoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cotoId"], unique = true)]
)
data class DatosSuelo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cotoId: Int,
    val tipoSuelo: String, // "Arcilloso", "Arenoso", "Orgánico", "Mixto"
    val phSuelo: Double, // pH del suelo (4.0 - 8.0)
    val drenaje: String, // "Bueno", "Regular", "Malo"
    val materiaOrganica: Double, // Porcentaje de materia orgánica (0-100%)
    val textura: String // "Fina", "Media", "Gruesa"
)



