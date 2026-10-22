package com.setometro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.entity.ObservacionCampo
import com.setometro.app.data.model.FieldObservationResult
import com.setometro.app.data.model.PointWeatherData
import com.setometro.app.data.repository.CotoRepository
import com.setometro.app.data.repository.ObservacionCampoRepository
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
    val weatherError: String? = null,
    val observaciones: List<ObservacionCampo> = emptyList(),
    val isSavingObservation: Boolean = false,
    val observationError: String? = null,
    val observationSaved: Boolean = false
)

@HiltViewModel
class DetalleCotoViewModel @Inject constructor(
    private val cotoRepository: CotoRepository,
    private val weatherRepository: WeatherRepository,
    private val observacionCampoRepository: ObservacionCampoRepository
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

                val observaciones = observacionCampoRepository.getForCoto(cotoId)
                _uiState.value = DetalleCotoUiState(
                    isLoading = true,
                    coto = coto,
                    observaciones = observaciones
                )
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

    fun saveObservation(
        speciesName: String,
        scientificName: String?,
        result: FieldObservationResult,
        observedAt: Long,
        specimenCount: Int?,
        note: String?
    ) {
        val coto = _uiState.value.coto
        if (coto == null) {
            _uiState.value = _uiState.value.copy(
                observationError = "No se puede guardar la observación sin una ubicación."
            )
            return
        }
        if (speciesName.isBlank()) {
            _uiState.value = _uiState.value.copy(
                observationError = "Indica la especie o selecciona «No identificada»."
            )
            return
        }
        if (specimenCount != null && specimenCount <= 0) {
            _uiState.value = _uiState.value.copy(
                observationError = "El número de ejemplares debe ser mayor que cero."
            )
            return
        }
        if (specimenCount != null && result != FieldObservationResult.FOUND) {
            _uiState.value = _uiState.value.copy(
                observationError = "Solo indica ejemplares cuando el resultado es «Encontré»."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSavingObservation = true,
                observationError = null,
                observationSaved = false
            )
            try {
                observacionCampoRepository.insert(
                    ObservacionCampo(
                        cotoId = coto.id,
                        nombreUbicacion = coto.nombre,
                        latitud = coto.latitud,
                        longitud = coto.longitud,
                        fuenteCoordenadas = coto.fuenteCoordenadas,
                        precisionCoordenadasMetros = coto.precisionCoordenadasMetros,
                        especieNombre = speciesName.trim(),
                        nombreCientifico = scientificName,
                        resultado = result.name,
                        fechaObservacion = observedAt,
                        ejemplares = specimenCount,
                        nota = note?.trim()?.takeIf(String::isNotEmpty)
                    )
                )
                _uiState.value = _uiState.value.copy(
                    observaciones = observacionCampoRepository.getForCoto(coto.id),
                    isSavingObservation = false,
                    observationSaved = true
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSavingObservation = false,
                    observationError = e.message
                        ?: "No se pudo guardar la observación en este dispositivo."
                )
            }
        }
    }

    fun acknowledgeObservationSaved() {
        _uiState.value = _uiState.value.copy(observationSaved = false)
    }

    fun clearObservationError() {
        _uiState.value = _uiState.value.copy(observationError = null)
    }
}
