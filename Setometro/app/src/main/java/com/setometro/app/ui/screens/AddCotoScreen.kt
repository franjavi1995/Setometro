package com.setometro.app.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.setometro.app.R
import com.setometro.app.data.model.CotoData
import com.setometro.app.data.model.PlaceSearchHit
import com.setometro.app.ui.viewmodel.AddCotoViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

private data class ChosenMapPoint(
    val latitude: Double,
    val longitude: Double,
    val sourceName: String? = null,
    val province: String? = null
)

private val treeChoices = listOf(
    "Pino",
    "Roble",
    "Quejigo",
    "Castaño",
    "Haya",
    "Encina",
    "Alcornoque",
    "Abedul",
    "Abeto",
    "Enebro",
    "Fresno",
    "Eucalipto",
    "Chopo",
    "Otro",
    "No lo sé"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddCotoScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddCotoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var otherTrees by remember { mutableStateOf("") }
    var selectedTrees by remember { mutableStateOf(setOf<String>()) }
    var description by remember { mutableStateOf("") }
    var characteristics by remember { mutableStateOf("") }
    var selectedPoint by remember { mutableStateOf<ChosenMapPoint?>(null) }
    val context = LocalContext.current

    LaunchedEffect(uiState.success) {
        if (uiState.success) onNavigateBack()
    }
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = context.packageName
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir punto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Busca un lugar o un coto y selecciónalo, o toca directamente el mapa.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.clearSearchResults()
                        },
                        label = { Text("Buscar lugar o coto") },
                        placeholder = { Text("Nombre o localidad") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = { viewModel.searchPlaces(searchQuery) }
                        )
                    )
                    Button(
                        onClick = { viewModel.searchPlaces(searchQuery) },
                        enabled = searchQuery.trim().length >= 3 && !uiState.isSearching
                    ) {
                        if (uiState.isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                    }
                }
                Text(
                    text = "Búsqueda con Nominatim. Datos © OpenStreetMap contributors (ODbL). " +
                        "Puede encontrar cotos incluidos en OSM, pero no es un catálogo oficial ni completo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "La búsqueda se envía a Nominatim solo al pulsar Buscar; la app no envía tu ubicación actual.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (uiState.searchError != null) {
                    Text(
                        text = uiState.searchError.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (uiState.searchComplete && uiState.searchResults.isEmpty()) {
                    Text(
                        text = "OpenStreetMap no devolvió resultados para esta búsqueda.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                uiState.searchResults.forEach { result ->
                    PlaceResultCard(
                        result = result,
                        onClick = {
                            selectedPoint = ChosenMapPoint(
                                latitude = result.latitude,
                                longitude = result.longitude,
                                sourceName = result.name,
                                province = result.province
                            )
                            if (name.isBlank()) name = result.name
                            if (province.isBlank()) province = result.province.orEmpty()
                        }
                    )
                }

                Text(
                    text = "Ubicación",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                PointSelectionMap(
                    selectedPoint = selectedPoint,
                    onSelectPoint = { point ->
                        selectedPoint = ChosenMapPoint(point.latitude, point.longitude)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                )
                Text(
                    text = selectedPoint?.let {
                        "Punto seleccionado: %.5f, %.5f".format(it.latitude, it.longitude)
                    } ?: "Toca el mapa para marcar el punto exacto.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Mapa © OpenStreetMap contributors",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (selectedPoint?.sourceName != null) {
                    Text(
                        text = "Resultado seleccionado en OpenStreetMap: ${selectedPoint?.sourceName}. " +
                            "Puedes ajustar la posición tocando el mapa.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del punto *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = name.isBlank()
                )
                OutlinedTextField(
                    value = province,
                    onValueChange = { province = it },
                    label = { Text("Provincia o zona (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "¿Qué árboles hay en la zona? *",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Selecciona todos los que reconozcas. Es una observación tuya, " +
                        "no una identificación verificada por la app.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    treeChoices.forEach { tree ->
                        FilterChip(
                            selected = tree in selectedTrees,
                            onClick = {
                                selectedTrees = when (tree) {
                                    "No lo sé" -> if (tree in selectedTrees) {
                                        emptySet()
                                    } else {
                                        setOf(tree)
                                    }
                                    else -> if (tree in selectedTrees) {
                                        selectedTrees - tree
                                    } else {
                                        (selectedTrees - "No lo sé") + tree
                                    }
                                }
                            },
                            label = { Text(tree) }
                        )
                    }
                }
                if ("Otro" in selectedTrees) {
                    OutlinedTextField(
                        value = otherTrees,
                        onValueChange = { otherTrees = it.take(100) },
                        label = { Text("¿Qué otro árbol?") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it.take(500) },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                OutlinedTextField(
                    value = characteristics,
                    onValueChange = { characteristics = it.take(500) },
                    label = { Text("Características del terreno (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                if (uiState.error != null) {
                    Text(
                        text = uiState.error.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val point = selectedPoint ?: return@Button
                            val treeTypes = selectedTrees
                                .filterNot { it == "Otro" }
                                .sorted()
                                .toMutableList()
                            if ("Otro" in selectedTrees && otherTrees.isNotBlank()) {
                                treeTypes.add(otherTrees.trim())
                            }
                            viewModel.saveCoto(
                                CotoData(
                                    nombre = name.trim(),
                                    provincia = province.trim().ifBlank { "Sin especificar" },
                                    tipoBosque = treeTypes
                                        .takeIf { it.isNotEmpty() }
                                        ?.joinToString(", ")
                                        ?: "No lo sé",
                                    latitud = point.latitude,
                                    longitud = point.longitude,
                                    altitud = 0.0,
                                    descripcion = description.trim(),
                                    caracteristicas = characteristics.trim(),
                                    fuenteCoordenadas = "MAP"
                                )
                            )
                        },
                        enabled = name.isNotBlank() &&
                            selectedPoint != null &&
                            selectedTrees.isNotEmpty() &&
                            !uiState.isLoading &&
                            ("Otro" !in selectedTrees || otherTrees.isNotBlank()),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar punto")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun PlaceResultCard(result: PlaceSearchHit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(result.name, fontWeight = FontWeight.SemiBold)
            Text(result.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun PointSelectionMap(
    selectedPoint: ChosenMapPoint?,
    onSelectPoint: (GeoPoint) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { viewContext ->
            MapView(viewContext).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                minZoomLevel = 4.0
                maxZoomLevel = 19.0
                controller.setZoom(5.5)
                controller.setCenter(GeoPoint(40.4168, -3.7038))
            }
        },
        update = { map ->
            map.overlays.clear()
            map.overlays.add(
                MapEventsOverlay(
                    object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(point: GeoPoint): Boolean {
                            onSelectPoint(point)
                            return true
                        }

                        override fun longPressHelper(point: GeoPoint): Boolean {
                            onSelectPoint(point)
                            return true
                        }
                    }
                )
            )
            selectedPoint?.let { point ->
                val geoPoint = GeoPoint(point.latitude, point.longitude)
                val cameraKey = "${point.latitude},${point.longitude}"
                if (map.tag != cameraKey) {
                    map.tag = cameraKey
                    map.controller.setZoom(15.0)
                    map.controller.setCenter(geoPoint)
                }
                map.overlays.add(
                    Marker(map).apply {
                        position = geoPoint
                        title = point.sourceName ?: "Punto seleccionado"
                        icon = ContextCompat.getDrawable(
                            map.context,
                            R.drawable.ic_neutral_map_marker
                        )
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                )
            } ?: run {
                map.tag = null
            }
            map.invalidate()
        },
        modifier = modifier.clipToBounds()
    )
}
