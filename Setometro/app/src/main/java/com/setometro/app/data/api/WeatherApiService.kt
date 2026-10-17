package com.setometro.app.data.api

import com.setometro.app.data.model.OpenMeteoWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast")
    suspend fun getWeatherData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String =
            "temperature_2m,relative_humidity_2m,precipitation",
        @Query("daily") daily: String =
            "precipitation_sum,temperature_2m_mean,temperature_2m_min,temperature_2m_max,relative_humidity_2m_mean",
        @Query("past_days") pastDays: Int = 15,
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("timezone") timezone: String = "auto"
    ): Response<OpenMeteoWeatherResponse>
}
