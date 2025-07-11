package com.anthonyla.paperize.feature.wallpaper.presentation.themes

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

private val AmoledDarkColors = darkColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = Color.Black,
    onSecondary = Color.White,
    secondaryContainer = Color.Black,
    onSecondaryContainer = Color.White,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = Color.Black,
    onTertiaryContainer = Color.White,
    error = Color.Red,
    errorContainer = Color.Black,
    onError = Color.White,
    onErrorContainer = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color.Black,
    onSurfaceVariant = Color.White,
    outline = Color.White,
    inverseOnSurface = Color.Black,
    inverseSurface = Color.White,
    inversePrimary = Color.White,
    surfaceTint = Color.Black,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)


/**
 * App theming for dynamic theming when supported and dark mode.
 */
@Composable
fun PaperizeTheme(
    darkMode: Boolean?,
    amoledMode: Boolean,
    dynamicTheming: Boolean,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDarkMode = isDarkMode(darkMode = darkMode)
    val isDynamicTheming = isDynamicTheming(dynamicTheming = dynamicTheming)
    val dynamicThemingSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors = when {
        isDynamicTheming && dynamicThemingSupported && isDarkMode -> dynamicDarkColorScheme(context)
        isDynamicTheming && dynamicThemingSupported && !isDarkMode -> dynamicLightColorScheme(context)
        isDarkMode && amoledMode -> AmoledDarkColors
        isDarkMode && !amoledMode -> DarkColors
        else -> LightColors
    }

    // Set the status bar color and system bar style to transparent
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkMode
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkMode
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content,
        shapes = MaterialTheme.shapes
    )
}

@Composable
private fun isDarkMode(darkMode: Boolean?): Boolean =
    when(darkMode) {
        true -> true
        false -> false
        else -> isSystemInDarkTheme()
    }

@Composable
private fun isDynamicTheming(dynamicTheming: Boolean): Boolean =
    when(dynamicTheming) {
        true -> true
        false -> false
    }