package com.setometro.app.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.hilt.navigation.compose.hiltViewModel
import com.setometro.app.data.entity.Coto
import com.setometro.app.data.entity.ObservacionCampo
import com.setometro.app.data.model.DailyWeatherData
import com.setometro.app.data.model.FieldObservationResult
import com.setometro.app.data.model.ObservedSpecies
import com.setometro.app.data.model.PointWeatherData
import com.setometro.app.data.model.documentedObservedSpecies
import com.setometro.app.ui.components.RainfallChartCard
import com.setometro.app.ui.viewmodel.DetalleCotoViewModel
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

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
                observations = uiState.observaciones,
                isSavingObservation = uiState.isSavingObservation,
                observationError = uiState.observationError,
                observationSaved = uiState.observationSaved,
                onSaveObservation = viewModel::saveObservation,
                onClearObservationError = viewModel::clearObservationError,
                onObservationSavedAcknowledged = viewModel::acknowledgeObservationSaved,
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
    observations: List<ObservacionCampo>,
    isSavingObservation: Boolean,
    observationError: String?,
    observationSaved: Boolean,
    onSaveObservation: (
        String,
        String?,
        FieldObservationResult,
        Long,
        Int?,
        String?
    ) -> Unit,
    onClearObservationError: () -> Unit,
    onObservationSavedAcknowledged: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showObservationDialog by remember { mutableStateOf(false) }
    LaunchedEffect(observationSaved) {
        if (observationSaved) {
            showObservationDialog = false
            onObservationSavedAcknowledged()
        }
    }

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
        item {
            FieldObservationsCard(
                observations = observations,
                onAddObservation = {
                    showObservationDialog = true
                    onClearObservationError()
                }
            )
        }
    }

    if (showObservationDialog) {
        FieldObservationDialog(
            isSaving = isSavingObservation,
            error = observationError,
            onDismiss = { if (!isSavingObservation) showObservationDialog = false },
            onSave = onSaveObservation
        )
    }
}

@Composable
private fun FieldObservationsCard(
    observations: List<ObservacionCampo>,
    onAddObservation: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Observaciones de campo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Registra lo que encuentres para validar futuras orientaciones. " +
                    "Se guarda solo en este dispositivo; no se envía a ningún servidor.",
                style = MaterialTheme.typography.bodySmall
            )
            Button(onClick = onAddObservation) {
                Text("Añadir observación")
            }
            if (observations.isEmpty()) {
                Text(
                    text = "Todavía no hay observaciones para este punto.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Divider()
                observations.forEach { observation ->
                    ObservationSummary(observation)
                }
            }
        }
    }
}

@Composable
private fun ObservationSummary(observation: ObservacionCampo) {
    val result = FieldObservationResult.values()
        .firstOrNull { it.name == observation.resultado }
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = "${observation.especieNombre} · ${result?.label ?: "Resultado desconocido"}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        observation.nombreCientifico?.let {
            Text(text = it, style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = DateFormat.getDateTimeInstance().format(Date(observation.fechaObservacion)),
            style = MaterialTheme.typography.bodySmall
        )
        observation.ejemplares?.let {
            Text(text = "Ejemplares aproximados: $it", style = MaterialTheme.typography.bodySmall)
        }
        observation.nota?.let {
            Text(text = it, style = MaterialTheme.typography.bodySmall)
        }
        result?.let {
            Text(
                text = it.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FieldObservationDialog(
    isSaving: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onSave: (
        String,
        String?,
        FieldObservationResult,
        Long,
        Int?,
        String?
    ) -> Unit
) {
    val context = LocalContext.current
    var selectedSpecies by remember { mutableStateOf<ObservedSpecies?>(null) }
    var speciesMenuExpanded by remember { mutableStateOf(false) }
    var customSpecies by remember { mutableStateOf("") }
    var selectedResult by remember { mutableStateOf<FieldObservationResult?>(null) }
    var observedAt by remember { mutableStateOf(System.currentTimeMillis()) }
    var specimenCount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val speciesChoices = remember {
        listOf(ObservedSpecies("No identificada", null)) +
            documentedObservedSpecies +
            ObservedSpecies("Otra especie", null)
    }
    val speciesName = when (selectedSpecies?.name) {
        "No identificada" -> "No identificada"
        "Otra especie" -> customSpecies.trim()
        else -> selectedSpecies?.name.orEmpty()
    }
    val parsedCount = specimenCount.toIntOrNull()
    val countIsValid = specimenCount.isBlank() || parsedCount != null && parsedCount > 0
    val canSave = selectedSpecies != null &&
        speciesName.isNotBlank() &&
        selectedResult != null &&
        countIsValid &&
        !isSaving

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva observación") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                    Text(
                        "La especie y el resultado son datos que indicas tú; " +
                            "la aplicación no verifica la identificación."
                    )
                    ExposedDropdownMenuBox(
                        expanded = speciesMenuExpanded,
                        onExpandedChange = { speciesMenuExpanded = !speciesMenuExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedSpecies?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Especie observada") },
                            placeholder = { Text("Seleccionar") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = speciesMenuExpanded
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = speciesMenuExpanded,
                            onDismissRequest = { speciesMenuExpanded = false }
                        ) {
                            speciesChoices.forEach { species ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(species.name)
                                            species.scientificName?.let {
                                                Text(it, style = MaterialTheme.typography.bodySmall)
                                            }
                                        }
                                    },
                                    onClick = {
                                        selectedSpecies = species
                                        speciesMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    if (selectedSpecies?.name == "Otra especie") {
                        OutlinedTextField(
                            value = customSpecies,
                            onValueChange = { customSpecies = it.take(100) },
                            label = { Text("Nombre indicado") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    Text("Resultado de la visita", fontWeight = FontWeight.SemiBold)
                    FieldObservationResult.values().forEach { result ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedResult = result
                                    if (result != FieldObservationResult.FOUND) {
                                        specimenCount = ""
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedResult == result,
                                onClick = { selectedResult = result }
                            )
                            Column {
                                Text(result.label)
                                Text(
                                    result.explanation,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            val current = Calendar.getInstance().apply {
                                timeInMillis = observedAt
                            }
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    current.set(Calendar.YEAR, year)
                                    current.set(Calendar.MONTH, month)
                                    current.set(Calendar.DAY_OF_MONTH, day)
                                    TimePickerDialog(
                                        context,
                                        { _, hourOfDay, minute ->
                                            current.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                            current.set(Calendar.MINUTE, minute)
                                            current.set(Calendar.SECOND, 0)
                                            current.set(Calendar.MILLISECOND, 0)
                                            observedAt = current.timeInMillis
                                        },
                                        current.get(Calendar.HOUR_OF_DAY),
                                        current.get(Calendar.MINUTE),
                                        true
                                    ).show()
                                },
                                current.get(Calendar.YEAR),
                                current.get(Calendar.MONTH),
                                current.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Fecha y hora: ${DateFormat.getDateTimeInstance().format(Date(observedAt))}")
                    }
                    if (selectedResult == FieldObservationResult.FOUND) {
                        OutlinedTextField(
                            value = specimenCount,
                            onValueChange = { specimenCount = it.filter(Char::isDigit).take(5) },
                            label = { Text("Número aproximado de ejemplares (opcional)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = !countIsValid
                        )
                        if (!countIsValid) {
                            Text(
                                "Indica un número mayor que cero o deja el campo vacío.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it.take(500) },
                        label = { Text("Nota (opcional)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp),
                        minLines = 2
                    )
                    if (error != null) {
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
            }
        },
        confirmButton = {
                TextButton(
                    enabled = canSave,
                    onClick = {
                        val species = selectedSpecies ?: return@TextButton
                        val result = selectedResult ?: return@TextButton
                        onSave(
                            speciesName,
                            species.scientificName,
                            result,
                            observedAt,
                            parsedCount,
                            note
                        )
                    }
                ) {
                    Text(if (isSaving) "Guardando…" else "Guardar")
                }
        },
        dismissButton = {
                TextButton(onClick = onDismiss, enabled = !isSaving) {
                    Text("Cancelar")
                }
        }
    )
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
