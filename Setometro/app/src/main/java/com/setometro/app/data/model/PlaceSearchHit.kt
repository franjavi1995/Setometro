package com.setometro.app.data.model

data class PlaceSearchHit(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val province: String?
)
