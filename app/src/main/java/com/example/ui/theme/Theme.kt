package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color(0xFF1B5E20),
    primaryContainer = WebForestGreen,
    onPrimaryContainer = Color.White,
    secondary = WebGoldenAmber,
    onSecondary = Color.White,
    tertiary = WebGoldenAmber,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color(0xFFEDEDED),
    onSurface = Color(0xFFEDEDED)
)

private val LightColorScheme = lightColorScheme(
    primary = WebForestGreen,
    onPrimary = Color.White,
    primaryContainer = WebForestGreenLight,
    onPrimaryContainer = WebForestGreenDark,
    secondary = WebGoldenAmber,
    onSecondary = Color.White,
    secondaryContainer = WebGoldenAmberLight,
    onSecondaryContainer = WebGoldenAmberDark,
    tertiary = WebGoldenAmber,
    onTertiary = Color.White,
    tertiaryContainer = WebGoldenAmberLight,
    onTertiaryContainer = Color(0xFFC77E0C),
    background = WebBackground,
    surface = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,
    outline = WebBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
