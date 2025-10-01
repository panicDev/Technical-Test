package com.android.technicaltest.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightMinimalColors = lightColorScheme(
    primary = Color(0xFF3366FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Color(0xFF001A43),
    secondary = Color(0xFF4A5568),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = Color(0xFF1A202C),
    tertiary = Color(0xFF5B8DEF),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE2ECFF),
    onTertiaryContainer = Color(0xFF0D2046),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFF8FAFC),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5F5),
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFFE4E6),
    onErrorContainer = Color(0xFF7F1D1D)
)

private val DarkMinimalColors = darkColorScheme(
    primary = Color(0xFF9AB6FF),
    onPrimary = Color(0xFF002566),
    primaryContainer = Color(0xFF183463),
    onPrimaryContainer = Color(0xFFDCE2FF),
    secondary = Color(0xFFCBD5F5),
    onSecondary = Color(0xFF1E293B),
    secondaryContainer = Color(0xFF334155),
    onSecondaryContainer = Color(0xFFE2E8F0),
    tertiary = Color(0xFFADC6FF),
    onTertiary = Color(0xFF0F1D3C),
    tertiaryContainer = Color(0xFF253559),
    onTertiaryContainer = Color(0xFFE2ECFF),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFCBD5F5),
    outline = Color(0xFF475569),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

/**
 * Applies the minimalist Material 3 color palette and default typography to the entire app.
 *
 * @param darkTheme Indicates whether the dark color scheme should be used.
 * @param content The composable hierarchy that will inherit the theme styling.
 */
@Composable
fun MinimalistAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkMinimalColors else LightMinimalColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
