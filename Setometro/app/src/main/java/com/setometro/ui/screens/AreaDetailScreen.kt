package com.setometro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.setometro.data.MushroomArea
import com.setometro.viewmodel.MainViewModel

@Composable
fun AreaDetailScreen(
    mushroomArea: MushroomArea,
    viewModel: MainViewModel
) {
    val weatherData = viewModel.getWeatherData(mushroomArea)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(mushroomArea.name) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Weather Data", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            WeatherCard(weatherData)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Mushroom Probability: ${viewModel.calculateMushroomProbability(mushroomArea)}%", style = MaterialTheme.typography.h6)
        }
    }
}