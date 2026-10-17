package com.setometro.app.data.repository

import com.setometro.app.data.api.WeatherApiService
import com.setometro.app.data.model.DailyWeatherData
import com.setometro.app.data.model.PointWeatherData
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApiService: WeatherApiService
) {
    suspend fun fetchWeather(
        latitude: Double,
        longitude: Double
    ): PointWeatherData {
        val response = weatherApiService.getWeatherData(
            latitude = latitude,
            longitude = longitude
        )
        if (!response.isSuccessful) {
            throw IOException("Open-Meteo devolvió el error HTTP ${response.code()}.")
        }

        val body = response.body()
            ?: throw IOException("Open-Meteo respondió sin datos meteorológicos.")
        val current = body.current
            ?: throw IOException("Open-Meteo no devolvió las condiciones actuales.")
        val currentDate = current.time
            ?.substringBefore('T')
            ?.let(LocalDate::parse)
            ?: throw IOException("Open-Meteo no indicó la fecha de las condiciones actuales.")
        val daily = body.daily
            ?: throw IOException("Open-Meteo no devolvió el historial diario.")
        val dates = daily.time
            ?: throw IOException("Open-Meteo no devolvió las fechas del historial.")
        val precipitation = daily.precipitationSumMm
            ?: throw IOException("Open-Meteo no devolvió la precipitación diaria.")
        val meanTemperature = daily.meanTemperatureCelsius
            ?: throw IOException("Open-Meteo no devolvió la temperatura diaria.")

        if (precipitation.size != dates.size || meanTemperature.size != dates.size) {
            throw IOException("Open-Meteo devolvió series diarias incompletas.")
        }
        if (listOfNotNull(
                daily.minimumTemperatureCelsius,
                daily.maximumTemperatureCelsius,
                daily.meanRelativeHumidity
            ).any { it.size != dates.size }
        ) {
            throw IOException("Open-Meteo devolvió variables diarias con longitudes distintas.")
        }

        val previousDays = dates.indices
            .filter { index -> LocalDate.parse(dates[index]).isBefore(currentDate) }
            .map { index ->
                DailyWeatherData(
                    date = dates[index],
                    precipitationMm = precipitation[index],
                    meanTemperatureCelsius = meanTemperature[index],
                    minimumTemperatureCelsius = daily.minimumTemperatureCelsius?.get(index),
                    maximumTemperatureCelsius = daily.maximumTemperatureCelsius?.get(index),
                    meanRelativeHumidity = daily.meanRelativeHumidity?.get(index)
                )
            }

        if (previousDays.isEmpty()) {
            throw IOException("Open-Meteo no devolvió días completos anteriores a hoy.")
        }

        return PointWeatherData(
            requestedLatitude = latitude,
            requestedLongitude = longitude,
            modelLatitude = body.latitude
                ?: throw IOException("Open-Meteo no indicó la latitud de su modelo."),
            modelLongitude = body.longitude
                ?: throw IOException("Open-Meteo no indicó la longitud de su modelo."),
            modelElevationMeters = body.elevation,
            timezone = body.timezone
                ?: throw IOException("Open-Meteo no indicó la zona horaria."),
            current = current,
            previousDays = previousDays,
            retrievedAt = Instant.now().toString()
        )
    }
}
