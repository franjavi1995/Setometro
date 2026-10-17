package com.setometro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.model.PointWeatherData
import com.setometro.app.data.repository.CotoRepository
import com.setometro.app.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetalleCotoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val coto: Coto? = null,
    val weather: PointWeatherData? = null,
    val weatherError: String? = null
)

@HiltViewModel
class DetalleCotoViewModel @Inject constructor(
    private val cotoRepository: CotoRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleCotoUiState())
    val uiState: StateFlow<DetalleCotoUiState> = _uiState.asStateFlow()

    fun loadCotoDetails(cotoId: Int) {
        viewModelScope.launch {
            _uiState.value = DetalleCotoUiState(isLoading = true)

            try {
                val coto = cotoRepository.getCotoById(cotoId)
                if (coto == null) {
                    _uiState.value = DetalleCotoUiState(error = "Coto no encontrado.")
                    return@launch
                }

                _uiState.value = DetalleCotoUiState(isLoading = true, coto = coto)
                try {
                    val weather = weatherRepository.fetchWeather(coto.latitud, coto.longitud)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weather = weather
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weatherError = e.message ?: "No se pudieron obtener datos meteorológicos."
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = DetalleCotoUiState(
                    error = e.message ?: "No se pudo cargar el coto."
                )
            }
        }
    }

    fun refreshData() {
        _uiState.value.coto?.let { loadCotoDetails(it.id) }
    }
}
