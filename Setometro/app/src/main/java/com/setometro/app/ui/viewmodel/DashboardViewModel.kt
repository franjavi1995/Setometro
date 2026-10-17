package com.setometro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.repository.CotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val cotosSeleccionados: List<Coto> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val cotoRepository: CotoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadCotosSeleccionados()
    }

    private fun loadCotosSeleccionados() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState(isLoading = true)
            try {
                cotoRepository.getCotosSeleccionados().collect { cotos ->
                    _uiState.value = DashboardUiState(
                        cotosSeleccionados = cotos,
                        isLoading = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = DashboardUiState(
                    isLoading = false,
                    error = e.message ?: "No se pudieron cargar las ubicaciones guardadas."
                )
            }
        }
    }

    fun refreshData() {
        loadCotosSeleccionados()
    }
}
