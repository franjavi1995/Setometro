package com.setometro.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.setometro.app.data.model.MushroomKnowledgeEntry
import com.setometro.app.data.model.MushroomKnowledgeSource
import com.setometro.app.data.model.mushroomKnowledgeBook

@Composable
fun SetasScreen() {
    var expandedEntries by remember { mutableStateOf(setOf<String>()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Libro de conocimiento micológico",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Lo recopilado sobre hábitat, estación y condiciones relacionadas " +
                        "con la fructificación de cada especie, con sus fuentes y límites.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Importante para interpretar el libro",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Las fuentes revisadas no proporcionan todavía requisitos " +
                                "numéricos universales que permitan calcular si las condiciones " +
                                "son ideales para cada punto de España. Por eso aquí distinguimos " +
                                "lo observado de lo desconocido; estas fichas aún no generan una " +
                                "predicción ni ordenan tus ubicaciones.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Toca una especie para consultar la evidencia y abrir las fuentes.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        items(mushroomKnowledgeBook, key = { it.id }) { entry ->
            val expanded = entry.id in expandedEntries
            KnowledgeEntryCard(
                entry = entry,
                expanded = expanded,
                onToggle = {
                    expandedEntries = if (expanded) {
                        expandedEntries - entry.id
                    } else {
                        expandedEntries + entry.id
                    }
                }
            )
        }
        item {
            Text(
                text = "El mapa de conocimiento completo, con referencias bibliográficas y retos " +
                    "pendientes, está en MAPA_CONOCIMIENTO_MICOLOGICO.md.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun KnowledgeEntryCard(
    entry: MushroomKnowledgeEntry,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.commonName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = entry.scientificName,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                }
                Text(
                    text = if (expanded) "Cerrar ficha" else "Ver ficha",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = entry.evidenceLevel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (expanded) {
                Divider()
                KnowledgeListSection(
                    title = "Evidencia recopilada",
                    items = entry.knownEvidence
                )
                KnowledgeListSection(
                    title = "Factores que podrían interesar a la app",
                    items = entry.factorsRelevantToApp
                )
                KnowledgeListSection(
                    title = "Lo que aún no se sabe",
                    items = entry.unknowns
                )
                Text(
                    text = "Ámbito de las fuentes: ${entry.geographicScope}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Fuentes consultadas",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                entry.sources.forEach { source ->
                    KnowledgeSourceLink(source)
                }
            }
        }
    }
}

@Composable
private fun KnowledgeListSection(title: String, items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        items.forEach { item ->
            Text(
                text = "• $item",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun KnowledgeSourceLink(source: MushroomKnowledgeSource) {
    val uriHandler = LocalUriHandler.current
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = source.title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { uriHandler.openUri(source.url) }
        )
        Text(
            text = source.sourceType,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
