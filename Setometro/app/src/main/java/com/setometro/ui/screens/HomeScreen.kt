package com.setometro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.setometro.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Setómetro") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Mushroom Hunting Areas", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))

            val areas = viewModel.getMushroomAreas()
            for (area in areas) {
                WeatherCard(area = area, viewModel = viewModel)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}