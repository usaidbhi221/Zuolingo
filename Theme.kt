package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppThemeMode(val displayName: String, val description: String) {
    ZUOLINGO_GREEN_WHITE("Zuolingo Green (Default)", "Classic Duolingo vibrant green and white"),
    DARK("Dark Mode", "Deep slate night theme"),
    CUSTOM("Custom Color", "Personalized custom accent theme")
}

// 1. Default Zuolingo Green & White Theme
private val ZuolingoGreenWhiteColorScheme = lightColorScheme(
    primary = DuolingoGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7FFB8),
    onPrimaryContainer = DuolingoGreenDark,
    secondary = DuolingoGemBlue,
    onSecondary = Color.White,
    tertiary = DuolingoGoldCrown,
    background = DuolingoBackground,
    onBackground = DuolingoTextPrimary,
    surface = DuolingoSurface,
    onSurface = DuolingoTextPrimary,
    surfaceVariant = Color(0xFFF0FDF4),
    onSurfaceVariant = DuolingoTextSecondary,
    outline = Color.Transparent, // No harsh outlines!
    error = DuolingoHeartRed
)

// 2. Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = DuolingoGreen,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1E3A1A),
    onPrimaryContainer = Color(0xFF89E219),
    secondary = DuolingoGemBlue,
    onSecondary = Color.White,
    tertiary = DuolingoGoldCrown,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color.Transparent,
    error = DuolingoHeartRed
)

@Composable
fun ZuolingoTheme(
    themeMode: AppThemeMode = AppThemeMode.ZUOLINGO_GREEN_WHITE,
    customColor: Color = DuolingoGreen,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.ZUOLINGO_GREEN_WHITE -> ZuolingoGreenWhiteColorScheme

        AppThemeMode.DARK -> DarkColorScheme

        AppThemeMode.CUSTOM -> lightColorScheme(
            primary = customColor,
            onPrimary = Color.White,
            primaryContainer = customColor.copy(alpha = 0.2f),
            onPrimaryContainer = customColor,
            secondary = DuolingoGemBlue,
            onSecondary = Color.White,
            tertiary = DuolingoGoldCrown,
            background = DuolingoBackground,
            onBackground = DuolingoTextPrimary,
            surface = DuolingoSurface,
            onSurface = DuolingoTextPrimary,
            surfaceVariant = customColor.copy(alpha = 0.08f),
            onSurfaceVariant = DuolingoTextSecondary,
            outline = Color.Transparent,
            error = DuolingoHeartRed
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
