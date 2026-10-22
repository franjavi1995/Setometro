package com.setometro.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            KnowledgeBookHeader()
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
                text = "Este libro crece con fuentes verificables. La revisión completa y sus " +
                    "referencias están en MAPA_CONOCIMIENTO_MICOLOGICO.md.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun KnowledgeBookHeader() {
    val colors = MaterialTheme.colorScheme
    val illustrationStem = Color(0xFFF4E7CA)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = colors.primary),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "GUÍA DE CAMPO",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimary.copy(alpha = 0.78f)
                    )
                    Text(
                        text = "Conoce qué busca cada seta",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimary
                    )
                    Text(
                        text = "Hábitat, temporada, evidencia y lo que todavía no sabemos.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onPrimary.copy(alpha = 0.9f)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                MushroomIllustration(
                    modifier = Modifier.size(width = 88.dp, height = 96.dp),
                    capColor = Color(0xFFE3AE54),
                    stemColor = illustrationStem,
                    spotColor = Color.White
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BookMetric(
                value = mushroomKnowledgeBook.size.toString(),
                label = "especies documentadas",
                modifier = Modifier.weight(1f)
            )
            BookMetric(
                value = "0",
                label = "umbrales universales",
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = colors.secondary.copy(alpha = 0.12f)
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Aviso sobre la evidencia",
                    tint = colors.onSurface
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Conocimiento, no predicción",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                    Text(
                        text = "Las fuentes actuales aún no permiten calcular condiciones " +
                            "ideales para cualquier punto de España. Las fichas explican la " +
                            "evidencia disponible y sus límites; no generan porcentajes ni " +
                            "clasifican tus ubicaciones.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )
                }
            }
        }

        Text(
            text = "Fichas de especies",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp)
        )
        Text(
            text = "Abre una ficha para ver qué factores podría estudiar la app.",
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant
        )
    }
}

@Composable
private fun BookMetric(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
    val colors = MaterialTheme.colorScheme
    val isScientificEvidence = entry.evidenceLevel.contains("cient", ignoreCase = true)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClickLabel = if (expanded) "Contraer ficha" else "Ampliar ficha",
                onClick = onToggle
            )
            .semantics {
                stateDescription = if (expanded) "Ficha ampliada" else "Ficha contraída"
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (expanded) {
                colors.surface
            } else {
                colors.primary.copy(alpha = 0.055f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = colors.primary.copy(alpha = 0.12f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        MushroomIllustration(
                            modifier = Modifier.size(36.dp),
                            capColor = Color(0xFF8C5536),
                            stemColor = Color(0xFFF4E7CA),
                            spotColor = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.commonName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = entry.scientificName,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = colors.onSurfaceVariant
                    )
                }
                Text(
                    text = if (expanded) "−" else "+",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isScientificEvidence) {
                        Icons.Default.Star
                    } else {
                        Icons.Default.Info
                    },
                    contentDescription = if (isScientificEvidence) {
                        "Hay estudios científicos citados"
                    } else {
                        "La fuente es descriptiva"
                    },
                    modifier = Modifier.size(16.dp),
                    tint = if (isScientificEvidence) colors.primary else colors.secondary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = entry.evidenceLevel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isScientificEvidence) colors.primary else colors.secondary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (expanded) {
                Divider(color = colors.outlineVariant)
                KnowledgeListSection(
                    title = "Qué sabemos",
                    icon = KnowledgeSectionIcon.EVIDENCE,
                    items = entry.knownEvidence
                )
                KnowledgeListSection(
                    title = "Factores que podría estudiar Setómetro",
                    icon = KnowledgeSectionIcon.FACTORS,
                    items = entry.factorsRelevantToApp
                )
                KnowledgeListSection(
                    title = "Lo que falta por saber",
                    icon = KnowledgeSectionIcon.UNKNOWN,
                    items = entry.unknowns
                )
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = colors.primary.copy(alpha = 0.07f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Ámbito geográfico",
                            tint = colors.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                text = "Ámbito de las fuentes",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = entry.geographicScope,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant
                            )
                        }
                    }
                }
                SourcesSection(entry.sources)
            }
        }
    }
}

private enum class KnowledgeSectionIcon {
    EVIDENCE,
    FACTORS,
    UNKNOWN
}

@Composable
private fun KnowledgeListSection(
    title: String,
    icon: KnowledgeSectionIcon,
    items: List<String>
) {
    val colors = MaterialTheme.colorScheme
    val iconColor = when (icon) {
        KnowledgeSectionIcon.EVIDENCE -> colors.primary
        KnowledgeSectionIcon.FACTORS -> colors.secondary
        KnowledgeSectionIcon.UNKNOWN -> colors.error
    }
    val iconImage = when (icon) {
        KnowledgeSectionIcon.EVIDENCE -> Icons.Default.Star
        KnowledgeSectionIcon.FACTORS -> Icons.Default.Search
        KnowledgeSectionIcon.UNKNOWN -> Icons.Default.Info
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = iconImage,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
        items.forEach { item ->
            Row(
                modifier = Modifier.padding(start = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "•",
                    color = iconColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SourcesSection(sources: List<MushroomKnowledgeSource>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Fuentes consultadas",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        sources.forEach { source ->
            KnowledgeSourceLink(source)
        }
    }
}

@Composable
private fun KnowledgeSourceLink(source: MushroomKnowledgeSource) {
    val uriHandler = LocalUriHandler.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { uriHandler.openUri(source.url) },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Abrir fuente",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(9.dp))
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = source.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = source.sourceType,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MushroomIllustration(
    modifier: Modifier = Modifier,
    capColor: Color,
    stemColor: Color,
    spotColor: Color
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val stem = Path().apply {
            moveTo(width * 0.43f, height * 0.52f)
            quadraticBezierTo(width * 0.39f, height * 0.71f, width * 0.35f, height * 0.88f)
            quadraticBezierTo(width * 0.5f, height * 0.94f, width * 0.66f, height * 0.88f)
            quadraticBezierTo(width * 0.6f, height * 0.69f, width * 0.58f, height * 0.52f)
            close()
        }
        drawPath(stem, stemColor)

        val cap = Path().apply {
            moveTo(width * 0.08f, height * 0.53f)
            quadraticBezierTo(width * 0.15f, height * 0.14f, width * 0.5f, height * 0.1f)
            quadraticBezierTo(width * 0.85f, height * 0.14f, width * 0.92f, height * 0.53f)
            quadraticBezierTo(width * 0.5f, height * 0.63f, width * 0.08f, height * 0.53f)
            close()
        }
        drawPath(cap, capColor)
        drawPath(
            cap,
            color = stemColor.copy(alpha = 0.38f),
            style = Stroke(width = (width * 0.025f).coerceAtLeast(1.dp.toPx()))
        )
        drawCircle(
            color = spotColor,
            radius = width * 0.045f,
            center = Offset(width * 0.34f, height * 0.31f)
        )
        drawCircle(
            color = spotColor,
            radius = width * 0.035f,
            center = Offset(width * 0.62f, height * 0.25f)
        )
        drawCircle(
            color = spotColor,
            radius = width * 0.04f,
            center = Offset(width * 0.72f, height * 0.42f)
        )
        drawOval(
            color = stemColor.copy(alpha = 0.6f),
            topLeft = Offset(width * 0.23f, height * 0.87f),
            size = Size(width * 0.54f, height * 0.07f)
        )
    }
}
