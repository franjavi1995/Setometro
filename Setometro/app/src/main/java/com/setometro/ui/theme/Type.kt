package com.setometro.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

val fontFamily = FontFamily(
    Font(R.font.your_font_regular),
    Font(R.font.your_font_bold, FontWeight.Bold)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = fontFamily,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    ),
    h2 = TextStyle(
        fontFamily = fontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    h3 = TextStyle(
        fontFamily = fontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    body1 = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    body2 = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    button = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
)