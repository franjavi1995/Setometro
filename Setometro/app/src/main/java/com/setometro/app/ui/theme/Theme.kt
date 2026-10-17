package com.setometro.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.setometro.app.data.preferences.AppPreferences

private val DarkColorScheme = darkColorScheme(
    primary = ForestGreen,
    secondary = EarthBrown,
    tertiary = MushroomBeige
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = EarthBrown,
    tertiary = MushroomBeige
)

// Formas modernas con bordes redondos
private val ModernShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun SetometroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = ModernShapes,
        content = content
    )
}

@Composable
fun SetometroThemeWithPreferences(
    appPreferences: AppPreferences,
    content: @Composable () -> Unit
) {
    // Hacer el tema reactivo a los cambios usando StateFlow
    val isNightMode by appPreferences.isNightMode.collectAsState()
    val colorScheme = if (isNightMode) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = ModernShapes,
        content = content
    )
}
