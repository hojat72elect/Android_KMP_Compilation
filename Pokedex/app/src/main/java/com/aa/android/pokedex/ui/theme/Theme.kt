package com.aa.android.pokedex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = darkColors(
    primary = PokemonBlue,
    primaryVariant = PokemonBlueDark,
    secondary = PokemonYellow,

    onPrimary = Color.White,
    onSecondary = Color.Black,
    background = Color.White,
    surface = Color.LightGray,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val DarkColorPalette = lightColors(
    primary = PokemonBlue,
    primaryVariant = PokemonBlueDark,
    secondary = PokemonYellow,

    onPrimary = Color.White,

    background = Color.Black,
    surface = Color.DarkGray,
    onBackground = Color.White,
    onSurface = Color.White

    /* Other default colors to override
    onPrimary = Color.White,
    onSecondary = Color.Black,
    */
)

@Composable
fun PokedexTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}