package com.setometro.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    primaryVariant = SecondaryColor,
    secondary = AccentColor,
    background = BackgroundColor,
    surface = Color.White,
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = TextColor,
    onBackground = TextColor,
    onSurface = TextColor,
    onError = Color.White
)

@Composable
fun SetometroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}