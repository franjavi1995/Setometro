package com.setometro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.data.WeatherRepository
import com.setometro.data.Coto
import com.setometro.data.RegistroDiario
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    var weatherData = mutableStateOf<WeatherData?>(null)
        private set

    var mushroomProbability = mutableStateOf(0.0)
        private set

    private val _cotosSeleccionados = MutableStateFlow<List<Coto>>(emptyList())
    val cotosSeleccionados: StateFlow<List<Coto>> = _cotosSeleccionados

    private val _registros = MutableStateFlow<List<RegistroDiario>>(emptyList())
    val registros: StateFlow<List<RegistroDiario>> = _registros

    fun fetchWeatherData(areaId: String) {
        viewModelScope.launch {
            weatherData.value = weatherRepository.getWeatherData(areaId)
            calculateMushroomProbability()
        }
    }

    fun cargarCotosSeleccionados() {
        viewModelScope.launch {
            _cotosSeleccionados.value = weatherRepository.cotoDao.getSeleccionados()
        }
    }

    fun cargarRegistros(cotoId: Int) {
        viewModelScope.launch {
            _registros.value = weatherRepository.registroDao.getUltimos10(cotoId)
        }
    }

    fun insertarCoto(coto: Coto, onFinish: (() -> Unit)? = null) {
        viewModelScope.launch {
            weatherRepository.cotoDao.insertAll(coto)
            cargarCotosSeleccionados()
            onFinish?.invoke()
        }
    }

    private fun calculateMushroomProbability() {
        weatherData.value?.let { data ->
            // Simple probability calculation based on temperature and rain
            mushroomProbability.value = when {
                data.temperature > 15 && data.rain > 10 -> 0.9 // High probability
                data.temperature in 10..15 && data.rain in 5..10 -> 0.6 // Medium probability
                else -> 0.2 // Low probability
            }
        }
    }
}

data class WeatherData(
    val temperature: Double,
    val rain: Double
)