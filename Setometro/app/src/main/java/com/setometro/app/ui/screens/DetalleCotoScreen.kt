package com.setometro.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.model.DailyWeatherData
import com.setometro.app.data.model.PointWeatherData
import com.setometro.app.ui.components.RainfallChartCard
import com.setometro.app.ui.viewmodel.DetalleCotoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCotoScreen(
    cotoId: Int,
    onNavigateBack: () -> Unit,
    viewModel: DetalleCotoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(cotoId) {
        viewModel.loadCotoDetails(cotoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del punto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading && uiState.coto == null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            uiState.error != null -> DetalleCotoErrorScreen(
                error = uiState.error ?: "Error desconocido",
                onRetry = { viewModel.loadCotoDetails(cotoId) },
                modifier = Modifier.padding(paddingValues)
            )
            uiState.coto != null -> CotoDetailContent(
                coto = uiState.coto!!,
                weather = uiState.weather,
                weatherError = uiState.weatherError,
                isLoadingWeather = uiState.isLoading,
                onRetryWeather = viewModel::refreshData,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun CotoDetailContent(
    coto: Coto,
    weather: PointWeatherData?,
    weatherError: String?,
    isLoadingWeather: Boolean,
    onRetryWeather: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { CotoInfoCard(coto) }
        item {
            WeatherSummaryCard(
                weather = weather,
                error = weatherError,
                isLoading = isLoadingWeather,
                onRetry = onRetryWeather
            )
        }
        item {
            RainfallChartCard(registros = weather?.previousDays.orEmpty())
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Presencia de setas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Todavía no hay un modelo validado de presencia para este punto y especie. El tiempo meteorológico, por sí solo, no confirma que haya setas.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun CotoInfoCard(coto: Coto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = coto.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "📍 ${coto.provincia}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "🌲 Tipo de bosque indicado: ${coto.tipoBosque}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Este dato del coto no está verificado por una fuente de hábitat.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = when (coto.fuenteCoordenadas) {
                    "GPS" -> "Origen de coordenadas: GPS"
                    "MAP" -> "Origen de coordenadas: punto seleccionado en mapa"
                    else -> "Origen de coordenadas: entrada manual"
                },
                style = MaterialTheme.typography.bodySmall
            )
            coto.precisionCoordenadasMetros?.let { accuracy ->
                Text(
                    text = "Precisión estimada al guardar: ±%.0f m".format(accuracy),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(text = "Coordenadas: ${coto.latitud}, ${coto.longitud}")
            if (coto.descripcion.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = coto.descripcion, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun WeatherSummaryCard(
    weather: PointWeatherData?,
    error: String?,
    isLoading: Boolean,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Condiciones del punto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator()
                    Text("Consultando Open-Meteo para las coordenadas de este punto…")
                }
                weather == null -> {
                    Text(
                        text = error ?: "No hay datos meteorológicos para este punto.",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) { Text("Reintentar consulta") }
                }
                else -> {
                    Text("Tiempo actual (modelo): ${weather.current.time ?: "fecha no disponible"}")
                    Text(
                        text = weather.current.temperatureCelsius?.let { "Temperatura: %.1f °C".format(it) }
                            ?: "Temperatura: sin datos"
                    )
                    Text(
                        text = weather.current.relativeHumidity?.let { "Humedad relativa: %.0f %%".format(it) }
                            ?: "Humedad relativa: sin datos"
                    )
                    Text(
                        text = weather.current.precipitation?.let { "Precipitación actual: %.1f mm".format(it) }
                            ?: "Precipitación actual: sin datos"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Historial diario modelado: ${weather.previousDays.firstOrNull()?.date ?: "sin fechas"} – ${weather.previousDays.lastOrNull()?.date ?: "sin fechas"}"
                    )
                    Text(
                        text = "Coordenada de modelo: %.4f, %.4f".format(
                            weather.modelLatitude,
                            weather.modelLongitude
                        )
                    )
                    Text(
                        text = weather.modelElevationMeters?.let {
                            "Elevación del modelo meteorológico: %.0f m".format(it)
                        } ?: "Elevación del modelo: sin datos"
                    )
                    Text(
                        text = "Zona horaria: ${weather.timezone}. Consultado: ${weather.retrievedAt}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Los valores corresponden a datos meteorológicos de modelo para una cuadrícula cercana; no son mediciones de una estación situada exactamente aquí.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(
                        onClick = {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://open-meteo.com/")
                                )
                            )
                        }
                    ) {
                        Text("Datos meteorológicos: Open-Meteo · CC BY 4.0")
                    }
                    if (error != null) {
                        Text(
                            text = "No se pudo actualizar: $error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                        TextButton(onClick = onRetry) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleCotoErrorScreen(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = error, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}
