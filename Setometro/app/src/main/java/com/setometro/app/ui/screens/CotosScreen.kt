package com.setometro.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.setometro.app.ui.viewmodel.CotoViewModel

@Composable
fun CotosScreen(
    onNavigateToDetalle: (Int) -> Unit,
    onNavigateToAddCoto: () -> Unit,
    viewModel: CotoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🌲",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ubicaciones",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            FloatingActionButton(
                onClick = onNavigateToAddCoto,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir coto")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                val error = uiState.error ?: "Error desconocido"
                ErrorScreen(
                    error = error,
                    onRetry = { viewModel.refreshData() }
                )
            }
            uiState.cotos.isEmpty() -> {
                EmptyCotosScreen()
            }
            else -> {
                       CotosList(
                           cotos = uiState.cotos,
                           onToggleSeleccion = { coto -> viewModel.toggleSeleccion(coto) },
                           onNavigateToDetalle = onNavigateToDetalle,
                           onDeleteCoto = { coto -> viewModel.deleteCoto(coto) }
                       )
            }
        }
    }
}

@Composable
fun EmptyCotosScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "No hay ubicaciones guardadas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Añade un punto manualmente. La app no dispone de un inventario verificado de cotos.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CotosList(
    cotos: List<com.setometro.app.data.entity.Coto>,
    onToggleSeleccion: (com.setometro.app.data.entity.Coto) -> Unit,
    onNavigateToDetalle: (Int) -> Unit,
    onDeleteCoto: (com.setometro.app.data.entity.Coto) -> Unit
) {
    Column {
        Text(
            text = "Ubicaciones guardadas (${cotos.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn {
            items(cotos) { coto ->
                CotoCard(
                    coto = coto,
                    onToggleSeleccion = { onToggleSeleccion(coto) },
                    onNavigateToDetalle = { onNavigateToDetalle(coto.id) },
                    onDeleteCoto = { onDeleteCoto(coto) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotoCard(
    coto: com.setometro.app.data.entity.Coto,
    onToggleSeleccion: () -> Unit,
    onNavigateToDetalle: () -> Unit,
    onDeleteCoto: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onNavigateToDetalle
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = coto.nombre,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = coto.provincia,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = coto.tipoBosque,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDeleteCoto,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar coto",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Switch(
                        checked = coto.seleccionado,
                        onCheckedChange = { onToggleSeleccion() }
                    )
                }
            }
        }
    }
}