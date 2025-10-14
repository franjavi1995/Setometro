package com.setometro.ui.screens

import androidx.compose.foundation.layout.*;
import androidx.compose.material.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.unit.dp;
import com.setometro.viewmodel.MainViewModel;
import com.setometro.ui.components.WeatherCard;
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun DashboardScreen(mainViewModel: MainViewModel? = null) {
    val cotos by mainViewModel?.cotosSeleccionados?.collectAsState() ?: remember { mutableStateOf(emptyList()) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Cotos seleccionados", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(8.dp))
        if (cotos.isEmpty()) {
            Text("No hay cotos seleccionados.")
        } else {
            cotos.forEach { coto ->
                // Simulación de datos
                WeatherCard(
                    cotoNombre = coto.nombre,
                    lluvia = 20.0,
                    temperatura = 15.0,
                    probabilidad = "Media"
                )
            }
        }
    }
}
