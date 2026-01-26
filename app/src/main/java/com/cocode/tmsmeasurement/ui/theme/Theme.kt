package com.cocode.tmsmeasurement.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = OnDark,
    secondary = DarkSecondary,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = OnDark,
    tertiary = DarkTertiary,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = OnDark,
    surface = DarkSurface,
    onSurface = OnDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDark,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = Teal,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Mint,
    onPrimaryContainer = Ink,
    secondary = Accent,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = AccentSoft,
    onSecondaryContainer = Ink,
    tertiary = Ink,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    background = Sand,
    onBackground = Ink,
    surface = CardSurface,
    onSurface = Ink,
    surfaceVariant = Mint,
    onSurfaceVariant = Ink,
    outline = OutlineLight
)

@Composable
fun TMSMeasurementTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
