package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = TopZynOrange,
    onPrimary = TopZynWhite,
    primaryContainer = TopZynOrangeContainer,
    onPrimaryContainer = TopZynOrangeDark,
    secondary = TopZynDarkBlack,
    onSecondary = TopZynWhite,
    background = TopZynBackground,
    onBackground = TopZynTextPrimary,
    surface = TopZynSurface,
    onSurface = TopZynTextPrimary,
    surfaceVariant = TopZynBackground,
    onSurfaceVariant = TopZynTextSecondary,
    outline = TopZynCardBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = TopZynOrange,
    onPrimary = TopZynWhite,
    primaryContainer = TopZynOrangeDark,
    onPrimaryContainer = TopZynOrangeContainer,
    secondary = TopZynGold,
    onSecondary = TopZynDarkBlack,
    background = TopZynDarkBlack,
    onBackground = TopZynWhite,
    surface = TopZynDarkCard,
    onSurface = TopZynWhite,
    surfaceVariant = TopZynDarkSurface,
    onSurfaceVariant = TopZynTextMuted,
    outline = TopZynDarkSurface
)

@Composable
fun TopZynTheme(
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
