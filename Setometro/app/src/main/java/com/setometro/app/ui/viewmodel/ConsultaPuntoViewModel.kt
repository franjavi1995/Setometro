package com.setometro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.model.PointWeatherData
import com.setometro.app.data.repository.CotoRepository
import com.setometro.app.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ConsultaPuntoUiState(
    val isLoadingWeather: Boolean = true,
    val weather: PointWeatherData? = null,
    val weatherError: String? = null,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val saveError: String? = null
)

@HiltViewModel
class ConsultaPuntoViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val cotoRepository: CotoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConsultaPuntoUiState())
    val uiState: StateFlow<ConsultaPuntoUiState> = _uiState.asStateFlow()

    fun loadWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingWeather = true,
                weather = null,
                weatherError = null
            )
            try {
                val weather = weatherRepository.fetchWeather(latitude, longitude)
                _uiState.value = _uiState.value.copy(
                    isLoadingWeather = false,
                    weather = weather
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingWeather = false,
                    weatherError = exception.message
                        ?: "No se pudieron obtener datos meteorológicos."
                )
            }
        }
    }

    fun savePoint(
        name: String,
        latitude: Double,
        longitude: Double,
        source: String,
        accuracyMeters: Float?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, saveError = null)
            try {
                cotoRepository.insertCoto(
                    Coto(
                        nombre = name.trim(),
                        provincia = "Sin especificar",
                        tipoBosque = "Sin verificar",
                        latitud = latitude,
                        longitud = longitude,
                        // The legacy schema requires a value; zero is an unspecified sentinel.
                        altitud = 0.0,
                        descripcion = "",
                        caracteristicas = "",
                        fuenteCoordenadas = source,
                        precisionCoordenadasMetros = accuracyMeters
                    )
                )
                _uiState.value = _uiState.value.copy(isSaving = false, saved = true)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    saveError = exception.message ?: "No se pudo guardar el punto."
                )
            }
        }
    }

    fun retryWeather(latitude: Double, longitude: Double) = loadWeather(latitude, longitude)
}
