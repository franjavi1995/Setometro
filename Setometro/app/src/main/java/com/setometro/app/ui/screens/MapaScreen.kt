package com.setometro.app.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.setometro.app.R
import com.setometro.app.data.entity.Coto
import com.setometro.app.ui.viewmodel.CotoViewModel
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
fun MapaScreenWithViewModel(
    onNavigateToDetalle: (Int) -> Unit,
    onNavigateToConsultaPunto: (Double, Double, String, Float?) -> Unit,
    viewModel: CotoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var permissionError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = context.packageName
    }

    LaunchedEffect(uiState.currentLocation) {
        uiState.currentLocation?.let { location ->
            viewModel.consumeCurrentLocation()
            onNavigateToConsultaPunto(
                location.latitude,
                location.longitude,
                "GPS",
                location.accuracyMeters
            )
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { it }) {
            permissionError = null
            viewModel.requestCurrentLocation()
        } else {
            permissionError = "Sin permiso de ubicación no se puede consultar tu posición."
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Mapa de ubicaciones",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Text(
            text = "Mantén pulsado el mapa para consultar condiciones o usa tu ubicación. El mapa no indica presencia de setas.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    val hasPermission =
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        permissionError = null
                        viewModel.requestCurrentLocation()
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                enabled = !uiState.isLocating,
                modifier = Modifier.weight(1f)
            ) {
                if (uiState.isLocating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Localizando…")
                } else {
                    Text("Usar mi ubicación")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = viewModel::refreshData,
                modifier = Modifier.weight(1f)
            ) {
                Text("Actualizar puntos")
            }
        }

        val visibleError = permissionError ?: uiState.locationError
        if (visibleError != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = visibleError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
        if (uiState.error != null) {
            Text(
                text = "No se pudieron cargar los puntos guardados: ${uiState.error}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        if (uiState.isLoading) {
            Text(
                text = "Cargando puntos guardados…",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        SavedLocationsMap(
            cotos = uiState.cotos,
            onNavigateToDetalle = onNavigateToDetalle,
            onSelectPoint = { point ->
                onNavigateToConsultaPunto(point.latitude, point.longitude, "MAP", null)
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SavedLocationsMap(
    cotos: List<Coto>,
    onNavigateToDetalle: (Int) -> Unit,
    onSelectPoint: (GeoPoint) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                minZoomLevel = 4.0
                maxZoomLevel = 18.0
                controller.setZoom(5.5)
                controller.setCenter(GeoPoint(40.4168, -3.7038))
            }
        },
        update = { map ->
            map.overlays.clear()
            map.overlays.add(
                MapEventsOverlay(
                    object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(point: GeoPoint): Boolean = false

                        override fun longPressHelper(point: GeoPoint): Boolean {
                            onSelectPoint(point)
                            return true
                        }
                    }
                )
            )

            val locationsKey = cotos.map { Triple(it.id, it.latitud, it.longitud) }
            if (map.tag != locationsKey) {
                map.tag = locationsKey
                if (cotos.isNotEmpty()) {
                    val center = GeoPoint(
                        cotos.map { it.latitud }.average(),
                        cotos.map { it.longitud }.average()
                    )
                    map.controller.setZoom(if (cotos.size == 1) 10.0 else 6.0)
                    map.controller.setCenter(center)
                }
            }

            cotos.forEach { coto ->
                map.overlays.add(
                    Marker(map).apply {
                        position = GeoPoint(coto.latitud, coto.longitud)
                        title = coto.nombre
                        snippet = "${coto.provincia} · punto guardado"
                        icon = ContextCompat.getDrawable(map.context, R.drawable.ic_neutral_map_marker)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        setOnMarkerClickListener { _, _ ->
                            onNavigateToDetalle(coto.id)
                            true
                        }
                    }
                )
            }
            map.invalidate()
        },
        modifier = modifier.clipToBounds()
    )
}
