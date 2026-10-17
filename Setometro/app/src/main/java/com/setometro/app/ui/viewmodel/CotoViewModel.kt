package com.setometro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.app.data.location.CurrentLocationProvider
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.repository.CotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CotoUiState(
    val cotos: List<Coto> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isLocating: Boolean = false,
    val currentLocation: LocatedPoint? = null,
    val locationError: String? = null
)

data class LocatedPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float?
)

@HiltViewModel
class CotoViewModel @Inject constructor(
    private val cotoRepository: CotoRepository,
    private val currentLocationProvider: CurrentLocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(CotoUiState())
    val uiState: StateFlow<CotoUiState> = _uiState.asStateFlow()

    init {
        loadCotos()
    }

    private fun loadCotos() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                cotoRepository.getAllCotos().collect { cotos ->
                    _uiState.value = _uiState.value.copy(
                        cotos = cotos,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun toggleSeleccion(coto: Coto) {
        viewModelScope.launch {
            try {
                cotoRepository.updateSeleccion(coto.id, !coto.seleccionado)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar selección"
                )
            }
        }
    }

    fun deleteCoto(coto: Coto) {
        viewModelScope.launch {
            try {
                cotoRepository.deleteCoto(coto)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al eliminar coto"
                )
            }
        }
    }

    fun refreshData() {
        loadCotos()
    }

    fun requestCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLocating = true,
                currentLocation = null,
                locationError = null
            )
            try {
                val location = currentLocationProvider.getCurrentLocation()
                _uiState.value = _uiState.value.copy(
                    isLocating = false,
                    locationError = null,
                    currentLocation = LocatedPoint(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        accuracyMeters = location.takeIf { it.hasAccuracy() }?.accuracy
                    )
                )
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLocating = false,
                    locationError = e.message ?: "No se pudo obtener la ubicación actual."
                )
            }
        }
    }

    fun consumeCurrentLocation() {
        _uiState.value = _uiState.value.copy(currentLocation = null)
    }

}
