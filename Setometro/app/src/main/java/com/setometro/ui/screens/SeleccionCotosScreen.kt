package com.setometro.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SeleccionCotosScreen(navController: NavController? = null) {
    // TODO: Implementar mapa interactivo y lista de cotos
    Button(onClick = { navController?.navigate("nuevo_coto") }, modifier = Modifier) {
        Text("Añadir nuevo coto")
    }
}
