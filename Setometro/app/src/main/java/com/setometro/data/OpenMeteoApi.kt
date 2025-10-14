package com.setometro.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Modelo de respuesta simplificado
 data class OpenMeteoResponse(
    val daily: DailyData
)

data class DailyData(
    val precipitation_sum: List<Double>,
    val temperature_2m_mean: List<Double>,
    val time: List<String>
)

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("daily") daily: String = "precipitation_sum,temperature_2m_mean",
        @Query("timezone") timezone: String = "Europe/Madrid"
    ): OpenMeteoResponse
}

object OpenMeteoService {
    val api: OpenMeteoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoApi::class.java)
    }
}
