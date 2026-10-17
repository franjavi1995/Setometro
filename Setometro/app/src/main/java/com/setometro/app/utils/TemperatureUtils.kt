package com.setometro.app.utils

import com.setometro.app.data.preferences.AppPreferences

object TemperatureUtils {
    
    fun celsiusToFahrenheit(celsius: Double): Double {
        return (celsius * 9.0 / 5.0) + 32.0
    }
    
    fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32.0) * 5.0 / 9.0
    }
    
    fun formatTemperature(temperature: Double, isCelsius: Boolean): String {
        return if (isCelsius) {
            "${String.format("%.1f", temperature)}°C"
        } else {
            val fahrenheit = celsiusToFahrenheit(temperature)
            "${String.format("%.1f", fahrenheit)}°F"
        }
    }
    
    fun getTemperatureSymbol(isCelsius: Boolean): String {
        return if (isCelsius) "°C" else "°F"
    }
}


