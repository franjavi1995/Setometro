package com.setometro.data

import android.content.Context
import androidx.room.Room

class WeatherRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "setometro-db"
    ).build()

    val cotoDao = db.cotoDao()
    val registroDao = db.registroDiarioDao()
}