package com.setometro.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.setometro.ui.theme.PrimaryColor
import com.setometro.ui.theme.BackgroundColor
import com.setometro.ui.theme.TextColor

@Composable
fun WeatherCard(
    cotoNombre: String,
    lluvia: Double,
    temperatura: Double,
    probabilidad: String,
    modifier: Modifier = Modifier
) {
    val probColor = when (probabilidad.lowercase()) {
        "alta" -> Color(0xFF388E3C) // verde
        "media" -> Color(0xFFFFC107) // amarillo
        "baja" -> Color(0xFFD32F2F) // rojo
        else -> TextColor
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        border = BorderStroke(1.dp, PrimaryColor)
    ) {
        Column(
            modifier = Modifier
                .background(BackgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = cotoNombre, fontSize = 20.sp, color = PrimaryColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Lluvia: $lluvia mm", fontSize = 16.sp, color = Color.Blue)
            Text(text = "Temp: $temperatura °C", fontSize = 16.sp, color = Color.Red)
            Text(text = "Probabilidad: $probabilidad", fontSize = 16.sp, color = probColor)
        }
    }
}