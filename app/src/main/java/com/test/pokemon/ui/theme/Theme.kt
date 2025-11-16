package com.test.pokemon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Background,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White
)

@Composable
fun PokemonTheme(
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
