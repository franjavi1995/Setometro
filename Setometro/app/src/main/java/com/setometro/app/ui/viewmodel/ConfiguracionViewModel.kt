package com.setometro.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setometro.app.data.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConfiguracionUiState(
    val isNightMode: Boolean = false,
    val isCelsius: Boolean = true,
    val appVersion: String = "1.0.0",
    val databaseSize: String = "0 MB",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ConfiguracionViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfiguracionUiState())
    val uiState: StateFlow<ConfiguracionUiState> = _uiState.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isNightMode = appPreferences.isNightMode.value,
                    isCelsius = appPreferences.isCelsius.value,
                    appVersion = appPreferences.getAppVersion(),
                    databaseSize = appPreferences.getDatabaseSize(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar configuraciones"
                )
            }
        }
    }

    fun toggleNightMode() {
        viewModelScope.launch {
            try {
                val newValue = !_uiState.value.isNightMode
                appPreferences.setNightMode(newValue)
                _uiState.value = _uiState.value.copy(isNightMode = newValue)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cambiar modo noche: ${e.message}"
                )
            }
        }
    }

    fun toggleTemperatureUnit() {
        viewModelScope.launch {
            try {
                val newValue = !_uiState.value.isCelsius
                appPreferences.setCelsius(newValue)
                _uiState.value = _uiState.value.copy(isCelsius = newValue)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cambiar unidades: ${e.message}"
                )
            }
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            try {
                appPreferences.clearCache()
                _uiState.value = _uiState.value.copy(
                    databaseSize = appPreferences.getDatabaseSize()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al limpiar caché: ${e.message}"
                )
            }
        }
    }

    fun refreshData() {
        loadPreferences()
    }
}
