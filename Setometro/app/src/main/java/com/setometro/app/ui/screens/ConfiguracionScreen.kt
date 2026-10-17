package com.setometro.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.setometro.app.ui.viewmodel.ConfiguracionViewModel

@Composable
fun ConfiguracionScreen(
    viewModel: ConfiguracionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚙️",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Configuración",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        item {
            Text(
                text = "Personaliza tu experiencia con Setómetro",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Sección de Apariencia
        item {
            ConfiguracionSection(
                title = "🎨 Apariencia",
                content = {
                    ConfiguracionItem(
                        title = "Modo Noche",
                        subtitle = if (uiState.isNightMode) "Activado" else "Desactivado",
                        trailing = { 
                            Switch(
                                checked = uiState.isNightMode,
                                onCheckedChange = { viewModel.toggleNightMode() }
                            )
                        }
                    )
                }
            )
        }
        
        // Sección de Unidades
        item {
            ConfiguracionSection(
                title = "🌡️ Unidades",
                content = {
                    ConfiguracionItem(
                        title = "Temperatura",
                        subtitle = if (uiState.isCelsius) "Celsius (°C)" else "Fahrenheit (°F)",
                        trailing = { 
                            Switch(
                                checked = uiState.isCelsius,
                                onCheckedChange = { viewModel.toggleTemperatureUnit() }
                            )
                        }
                    )
                }
            )
        }
        
        // Sección de Información
        item {
            ConfiguracionSection(
                title = "ℹ️ Información",
                content = {
                    ConfiguracionItem(
                        title = "Versión de la aplicación",
                        subtitle = uiState.appVersion,
                        trailing = { 
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Info",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    ConfiguracionItem(
                        title = "Tamaño de base de datos",
                        subtitle = uiState.databaseSize,
                        trailing = { 
                            Text(
                                text = uiState.databaseSize,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                    ConfiguracionItem(
                        title = "Limpiar caché",
                        subtitle = "Liberar espacio de almacenamiento",
                        trailing = { 
                            IconButton(
                                onClick = { viewModel.clearCache() }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Limpiar caché",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }
            )
        }
        
        // Mostrar error si existe
        uiState.error?.let { error ->
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun ConfiguracionSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun ConfiguracionItem(
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        trailing()
    }
}


