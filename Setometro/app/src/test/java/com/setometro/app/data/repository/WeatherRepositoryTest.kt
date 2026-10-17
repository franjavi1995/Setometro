package com.setometro.app.data.repository

import com.setometro.app.data.api.WeatherApiService
import com.setometro.app.data.model.CurrentWeatherResponse
import com.setometro.app.data.model.DailyWeatherResponse
import com.setometro.app.data.model.OpenMeteoWeatherResponse
import java.io.IOException
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryTest {

    @Test
    fun mapsRealDailySeriesAndExcludesCurrentDayFromHistory() = runBlocking {
        val repository = WeatherRepository(FakeWeatherApiService(Response.success(validResponse())))

        val weather = repository.fetchWeather(38.68, -6.4)

        assertEquals(38.68, weather.requestedLatitude, 0.0)
        assertEquals(38.664455, weather.modelLatitude, 0.0)
        assertEquals(2, weather.previousDays.size)
        assertEquals("2026-10-15", weather.previousDays[0].date)
        assertEquals(1.2, weather.previousDays[0].precipitationMm!!, 0.0)
        assertNull(weather.previousDays[1].precipitationMm)
        assertEquals(18.6, weather.previousDays[1].meanTemperatureCelsius!!, 0.0)
    }

    @Test
    fun reportsHttpFailureInsteadOfReturningWeather() {
        val errorResponse = Response.error<OpenMeteoWeatherResponse>(
            503,
            "".toResponseBody("text/plain".toMediaType())
        )
        val repository = WeatherRepository(FakeWeatherApiService(errorResponse))

        val error = assertThrows(IOException::class.java) {
            runBlocking { repository.fetchWeather(38.68, -6.4) }
        }

        assertEquals("Open-Meteo devolvió el error HTTP 503.", error.message)
    }

    @Test
    fun rejectsDailySeriesWithMismatchedLengths() {
        val response = validResponse().copy(
            daily = validResponse().daily!!.copy(
                precipitationSumMm = listOf(1.2)
            )
        )
        val repository = WeatherRepository(FakeWeatherApiService(Response.success(response)))

        assertThrows(IOException::class.java) {
            runBlocking { repository.fetchWeather(38.68, -6.4) }
        }
    }

    private fun validResponse() = OpenMeteoWeatherResponse(
        latitude = 38.664455,
        longitude = -6.419972,
        elevation = 353.0,
        timezone = "Europe/Madrid",
        current = CurrentWeatherResponse(
            time = "2026-10-17T00:30",
            temperatureCelsius = 21.2,
            relativeHumidity = 56.0,
            precipitation = 0.0
        ),
        daily = DailyWeatherResponse(
            time = listOf("2026-10-15", "2026-10-16", "2026-10-17"),
            precipitationSumMm = listOf(1.2, null, 0.1),
            meanTemperatureCelsius = listOf(20.5, 18.6, 19.9),
            minimumTemperatureCelsius = listOf(16.8, 15.1, 15.8),
            maximumTemperatureCelsius = listOf(30.4, 23.0, 25.3),
            meanRelativeHumidity = listOf(58.0, 75.0, 65.0)
        )
    )

    private class FakeWeatherApiService(
        private val response: Response<OpenMeteoWeatherResponse>
    ) : WeatherApiService {
        override suspend fun getWeatherData(
            latitude: Double,
            longitude: Double,
            current: String,
            daily: String,
            pastDays: Int,
            forecastDays: Int,
            timezone: String
        ): Response<OpenMeteoWeatherResponse> = response
    }
}
