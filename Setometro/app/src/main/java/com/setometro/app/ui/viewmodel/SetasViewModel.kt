package com.setometro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.app.data.entity.EspecieSeta
import com.setometro.app.data.repository.EspecieSetaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SetasUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val especies: List<EspecieSeta> = emptyList()
)

@HiltViewModel
class SetasViewModel @Inject constructor(
    private val especieSetaRepository: EspecieSetaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetasUiState())
    val uiState: StateFlow<SetasUiState> = _uiState.asStateFlow()

    fun loadEspecies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                especieSetaRepository.getAllEspecies().collect { especies ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        especies = especies
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "No se pudo cargar el catálogo de especies."
                )
            }
        }
    }

    fun toggleEspecieActiva(especie: EspecieSeta) {
        viewModelScope.launch {
            try {
                especieSetaRepository.updateActiva(especie.id, !especie.activa)
                val especies = _uiState.value.especies.toMutableList()
                val index = especies.indexOfFirst { it.id == especie.id }
                if (index != -1) {
                    especies[index] = especie.copy(activa = !especie.activa)
                    _uiState.value = _uiState.value.copy(especies = especies)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo actualizar la especie: ${e.message}"
                )
            }
        }
    }

    fun refreshData() {
        loadEspecies()
    }
}
