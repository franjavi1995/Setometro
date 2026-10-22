package com.setometro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.model.CotoData
import com.setometro.app.data.model.PlaceSearchHit
import com.setometro.app.data.repository.CotoRepository
import com.setometro.app.data.repository.PlaceSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddCotoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val isSearching: Boolean = false,
    val searchResults: List<PlaceSearchHit> = emptyList(),
    val searchError: String? = null,
    val searchComplete: Boolean = false
)

@HiltViewModel
class AddCotoViewModel @Inject constructor(
    private val cotoRepository: CotoRepository,
    private val placeSearchRepository: PlaceSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCotoUiState())
    val uiState: StateFlow<AddCotoUiState> = _uiState.asStateFlow()
    private var searchGeneration = 0L

    fun saveCoto(cotoData: CotoData) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val coto = Coto(
                    nombre = cotoData.nombre,
                    provincia = cotoData.provincia,
                    tipoBosque = cotoData.tipoBosque,
                    latitud = cotoData.latitud,
                    longitud = cotoData.longitud,
                    altitud = cotoData.altitud,
                    descripcion = cotoData.descripcion,
                    caracteristicas = cotoData.caracteristicas,
                    seleccionado = false,
                    fuenteCoordenadas = cotoData.fuenteCoordenadas
                )
                
                cotoRepository.insertCoto(coto)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = true
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al guardar el coto"
                )
            }
        }
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(success = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun searchPlaces(query: String) {
        if (_uiState.value.isSearching) return
        val generation = ++searchGeneration
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSearching = true,
                searchError = null,
                searchResults = emptyList(),
                searchComplete = false
            )
            try {
                val results = placeSearchRepository.search(query)
                if (generation != searchGeneration) return@launch
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    searchResults = results,
                    searchComplete = true
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (generation != searchGeneration) return@launch
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    searchError = e.message ?: "No se pudo buscar en OpenStreetMap."
                )
            }
        }
    }

    fun clearSearchResults() {
        searchGeneration++
        _uiState.value = _uiState.value.copy(
            isSearching = false,
            searchResults = emptyList(),
            searchError = null,
            searchComplete = false
        )
    }
}



