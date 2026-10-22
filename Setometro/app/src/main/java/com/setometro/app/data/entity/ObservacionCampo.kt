package com.setometro.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "observaciones_campo",
    foreignKeys = [
        ForeignKey(
            entity = Coto::class,
            parentColumns = ["id"],
            childColumns = ["cotoId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("cotoId"), Index("fechaObservacion")]
)
data class ObservacionCampo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cotoId: Int?,
    val nombreUbicacion: String,
    val latitud: Double,
    val longitud: Double,
    val fuenteCoordenadas: String,
    val precisionCoordenadasMetros: Float?,
    val especieNombre: String,
    val nombreCientifico: String?,
    val resultado: String,
    val fechaObservacion: Long,
    val ejemplares: Int?,
    val nota: String?
)
