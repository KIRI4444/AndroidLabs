package ru.androidlabs.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PlaylistBlue = Color(0xFF3772E7)
val PlaylistBlack = Color(0xFF1A1B22)

private val LightColors = lightColorScheme(
    primary = PlaylistBlue,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = PlaylistBlack,
    onSurface = PlaylistBlack
)

@Composable
fun AndroidLabsTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, typography = Typography, content = content)
}
