package com.setometro.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.setometro.data.Coto

@Composable
fun MapaCotosScreen(
    cotos: List<Coto>,
    onCotoSeleccionado: (Coto) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        cotos.forEach { coto ->
            Marker(
                position = com.google.android.gms.maps.model.LatLng(coto.latitud, coto.longitud),
                title = coto.nombre,
                snippet = coto.tipo_bosque,
                onClick = { onCotoSeleccionado(coto); true }
            )
        }
    }
}
