package com.setometro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.setometro.data.Coto

@Composable
fun NuevoCotoScreen(
    onCotoCreado: (Coto) -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nombre by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var tipoBosque by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Text("Añadir nuevo coto", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = provincia, onValueChange = { provincia = it }, label = { Text("Provincia") })
        OutlinedTextField(value = tipoBosque, onValueChange = { tipoBosque = it }, label = { Text("Tipo de bosque") })
        OutlinedTextField(value = latitud, onValueChange = { latitud = it }, label = { Text("Latitud") })
        OutlinedTextField(value = longitud, onValueChange = { longitud = it }, label = { Text("Longitud") })
        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colors.error)
        }
        Spacer(Modifier.height(16.dp))
        Row {
            Button(onClick = {
                val lat = latitud.toDoubleOrNull()
                val lon = longitud.toDoubleOrNull()
                if (nombre.isBlank() || provincia.isBlank() || tipoBosque.isBlank() || lat == null || lon == null) {
                    error = "Todos los campos son obligatorios y lat/lon deben ser válidos."
                } else {
                    onCotoCreado(
                        Coto(
                            nombre = nombre,
                            provincia = provincia,
                            tipo_bosque = tipoBosque,
                            latitud = lat,
                            longitud = lon,
                            seleccionado = true
                        )
                    )
                }
            }) { Text("Guardar") }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onCancelar) { Text("Cancelar") }
        }
    }
}
