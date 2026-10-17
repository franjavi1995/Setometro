package com.setometro.app.data.model

import com.google.gson.annotations.SerializedName

data class OpenMeteoWeatherResponse(
    val latitude: Double?,
    val longitude: Double?,
    val elevation: Double?,
    val timezone: String?,
    val current: CurrentWeatherResponse?,
    val daily: DailyWeatherResponse?
)

data class CurrentWeatherResponse(
    val time: String?,
    @SerializedName("temperature_2m")
    val temperatureCelsius: Double?,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity: Double?,
    val precipitation: Double?
)

data class DailyWeatherResponse(
    val time: List<String>?,
    @SerializedName("precipitation_sum")
    val precipitationSumMm: List<Double?>?,
    @SerializedName("temperature_2m_mean")
    val meanTemperatureCelsius: List<Double?>?,
    @SerializedName("temperature_2m_min")
    val minimumTemperatureCelsius: List<Double?>?,
    @SerializedName("temperature_2m_max")
    val maximumTemperatureCelsius: List<Double?>?,
    @SerializedName("relative_humidity_2m_mean")
    val meanRelativeHumidity: List<Double?>?
)

data class DailyWeatherData(
    val date: String,
    val precipitationMm: Double?,
    val meanTemperatureCelsius: Double?,
    val minimumTemperatureCelsius: Double?,
    val maximumTemperatureCelsius: Double?,
    val meanRelativeHumidity: Double?
)

data class PointWeatherData(
    val requestedLatitude: Double,
    val requestedLongitude: Double,
    val modelLatitude: Double,
    val modelLongitude: Double,
    val modelElevationMeters: Double?,
    val timezone: String,
    val current: CurrentWeatherResponse,
    val previousDays: List<DailyWeatherData>,
    val retrievedAt: String
)
