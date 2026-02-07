package com.example.smartfixer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Brown80,
    secondary = Tan80,
    tertiary = OrangeAccent80,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color(0xFF1C1410),
    onSecondary = Color(0xFF1C1410),
    onTertiary = Color(0xFF1C1410),
    onBackground = Color(0xFFEDE0D4),
    onSurface = Color(0xFFEDE0D4)
)

private val LightColorScheme = lightColorScheme(
    primary = Brown40,
    secondary = Tan40,
    tertiary = OrangeAccent40,
    background = CreamBackground,
    surface = WarmWhiteSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DarkBrown,
    onSurface = DarkBrown
)

@Composable
fun SmartFixerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
