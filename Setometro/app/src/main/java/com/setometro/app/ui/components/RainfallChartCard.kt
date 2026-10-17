package com.setometro.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.setometro.app.data.model.DailyWeatherData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun RainfallChartCard(
    registros: List<DailyWeatherData>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Lluvia Diaria (Últimos 15 días)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (registros.none { it.precipitationMm != null }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay datos de lluvia disponibles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                RainfallChart(
                    registros = registros,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

@Composable
fun RainfallChart(
    registros: List<DailyWeatherData>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                setupChart()
            }
        },
        update = { chart ->
            updateChart(chart, registros)
        },
        modifier = modifier
    )
}

private fun BarChart.setupChart() {
    description.isEnabled = false
    setDrawGridBackground(false)
    setDrawBorders(false)
    
    // Configurar eje X
    xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        setDrawAxisLine(true)
        granularity = 1f
        labelCount = 7 // Mostrar máximo 7 etiquetas
    }
    
    // Configurar eje Y izquierdo
    axisLeft.apply {
        setDrawGridLines(true)
        setDrawAxisLine(true)
        axisMinimum = 0f
        granularity = 5f
    }
    
    // Ocultar eje Y derecho
    axisRight.isEnabled = false
    
    // Configurar leyenda
    legend.isEnabled = false
    
    // Configurar animación
    animateY(1000)
}

private fun updateChart(chart: BarChart, registros: List<DailyWeatherData>) {
    val entries = mutableListOf<BarEntry>()
    val labels = mutableListOf<String>()
    
    val sortedRegistros = registros.sortedBy { it.date }.takeLast(15)

    sortedRegistros.forEach { registro ->
        val precipitation = registro.precipitationMm ?: return@forEach
        val date = LocalDate.parse(registro.date)
        entries.add(BarEntry(entries.size.toFloat(), precipitation.toFloat()))
        labels.add(date.format(DateTimeFormatter.ofPattern("dd/MM")))
    }
    
    val dataSet = BarDataSet(entries, "Lluvia (mm)").apply {
        color = Color(0xFF4CAF50).toArgb() // Verde para lluvia
        setDrawValues(true)
        valueTextSize = 10f
        valueTextColor = Color.Black.toArgb()
    }
    
    val barData = BarData(dataSet).apply {
        barWidth = 0.8f
    }
    
    chart.data = barData
    
    // Configurar etiquetas del eje X
    chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
    
    // Refrescar el gráfico
    chart.invalidate()
}
