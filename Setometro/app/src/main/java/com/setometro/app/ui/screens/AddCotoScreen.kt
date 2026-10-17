package com.setometro.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.setometro.app.data.model.CotoData
import com.setometro.app.ui.viewmodel.AddCotoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCotoScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddCotoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Navegar hacia atrás cuando se guarde exitosamente
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onNavigateBack()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Nuevo Coto") },
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
                    AddCotoErrorScreen(
                        error = uiState.error!!,
                        onRetry = { viewModel.clearError() }
                    )
                }
                else -> {
                    AddCotoForm(
                        onSaveCoto = { cotoData ->
                            viewModel.saveCoto(cotoData)
                        },
                        onNavigateBack = onNavigateBack
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCotoForm(
    onSaveCoto: (CotoData) -> Unit,
    onNavigateBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var provincia by remember { mutableStateOf("") }
    var tipoBosque by remember { mutableStateOf("Pino") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var altitud by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var caracteristicas by remember { mutableStateOf("") }

    val tiposBosque = listOf("Pino", "Roble/Castaño/Haya", "Encina/Mixto")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Información del Coto",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Coto *") },
            modifier = Modifier.fillMaxWidth(),
            isError = nombre.isBlank()
        )
        
        OutlinedTextField(
            value = provincia,
            onValueChange = { provincia = it },
            label = { Text("Provincia *") },
            modifier = Modifier.fillMaxWidth(),
            isError = provincia.isBlank()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = tipoBosque,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Bosque *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tiposBosque.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            tipoBosque = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = latitud,
                onValueChange = { latitud = it },
                label = { Text("Latitud *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f),
                isError = latitud.isBlank() || latitud.toDoubleOrNull() == null
            )
            OutlinedTextField(
                value = longitud,
                onValueChange = { longitud = it },
                label = { Text("Longitud *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f),
                isError = longitud.isBlank() || longitud.toDoubleOrNull() == null
            )
        }
        
        OutlinedTextField(
            value = altitud,
            onValueChange = { altitud = it },
            label = { Text("Altitud (metros) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = altitud.isBlank() || altitud.toDoubleOrNull() == null
        )
        
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
        
        OutlinedTextField(
            value = caracteristicas,
            onValueChange = { caracteristicas = it },
            label = { Text("Características del terreno") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            
            val isFormValid = nombre.isNotBlank() && provincia.isNotBlank() && 
                             latitud.isNotBlank() && longitud.isNotBlank() && 
                             altitud.isNotBlank() &&
                             latitud.toDoubleOrNull() != null &&
                             longitud.toDoubleOrNull() != null &&
                             altitud.toDoubleOrNull() != null
            
            Button(
                onClick = {
                    if (isFormValid) {
                        val cotoData = CotoData(
                            nombre = nombre,
                            provincia = provincia,
                            tipoBosque = tipoBosque,
                            latitud = latitud.toDoubleOrNull() ?: 0.0,
                            longitud = longitud.toDoubleOrNull() ?: 0.0,
                            altitud = altitud.toDoubleOrNull() ?: 0.0,
                            descripcion = descripcion,
                            caracteristicas = caracteristicas
                        )
                        onSaveCoto(cotoData)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = isFormValid
            ) {
                Text("Guardar Coto")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "* Campos obligatorios",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AddCotoErrorScreen(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
