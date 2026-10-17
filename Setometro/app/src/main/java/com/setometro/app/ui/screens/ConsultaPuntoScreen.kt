package com.setometro.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.setometro.app.ui.components.RainfallChartCard
import com.setometro.app.ui.viewmodel.ConsultaPuntoViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ConsultaPuntoScreen(
    latitude: Double,
    longitude: Double,
    source: String,
    accuracyMeters: Float?,
    onNavigateBack: () -> Unit,
    viewModel: ConsultaPuntoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSaveDialog by remember { mutableStateOf(false) }
    var pointName by remember { mutableStateOf("") }

    LaunchedEffect(latitude, longitude) {
        viewModel.loadWeather(latitude, longitude)
    }
    LaunchedEffect(uiState.saved) {
        if (uiState.saved) showSaveDialog = false
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Guardar punto de monitoreo") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Se guardarán el nombre y las coordenadas. Provincia, altitud y tipo de bosque quedarán sin verificar."
                    )
                    OutlinedTextField(
                        value = pointName,
                        onValueChange = { pointName = it },
                        label = { Text("Nombre del punto") },
                        singleLine = true,
                        isError = pointName.isBlank()
                    )
                    uiState.saveError?.let { error ->
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.savePoint(
                            name = pointName,
                            latitude = latitude,
                            longitude = longitude,
                            source = source,
                            accuracyMeters = accuracyMeters
                        )
                    },
                    enabled = pointName.isNotBlank() && !uiState.isSaving
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator()
                    } else {
                        Text("Guardar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consultar punto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver al mapa"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PointLocationCard(
                    latitude = latitude,
                    longitude = longitude,
                    source = source,
                    accuracyMeters = accuracyMeters
                )
            }
            item {
                WeatherSummaryCard(
                    weather = uiState.weather,
                    error = uiState.weatherError,
                    isLoading = uiState.isLoadingWeather,
                    onRetry = { viewModel.retryWeather(latitude, longitude) }
                )
            }
            item { RainfallChartCard(registros = uiState.weather?.previousDays.orEmpty()) }
            item {
                Column {
                    Text(
                        text = "Presencia de setas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No hay un modelo validado de presencia para este punto. Las condiciones meteorológicas no confirman que haya setas."
                    )
                }
            }
            item {
                when {
                    uiState.saved -> Text(
                        "Punto guardado en tus ubicaciones de monitoreo.",
                        color = MaterialTheme.colorScheme.primary
                    )
                    else -> Button(
                        onClick = { showSaveDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar punto")
                    }
                }
            }
        }
    }
}

@Composable
private fun PointLocationCard(
    latitude: Double,
    longitude: Double,
    source: String,
    accuracyMeters: Float?
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Punto consultado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text("Coordenadas solicitadas: %.6f, %.6f".format(latitude, longitude))
            Text(
                when (source) {
                    "GPS" -> "Origen: ubicación actual del dispositivo"
                    "MAP" -> "Origen: punto elegido manteniendo pulsado el mapa"
                    else -> "Origen: coordenadas proporcionadas"
                }
            )
            if (source == "GPS") {
                Text(
                    accuracyMeters?.let {
                        "Precisión estimada por el dispositivo: ±%.0f m".format(it)
                    } ?: "El dispositivo no informó de la precisión."
                )
            }
            Text(
                "La consulta usa estas coordenadas de entrada; Open-Meteo devuelve datos de modelo para una cuadrícula cercana.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
