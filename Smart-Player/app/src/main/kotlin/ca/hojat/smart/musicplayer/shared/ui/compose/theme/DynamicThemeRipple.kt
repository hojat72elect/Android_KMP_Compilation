package ca.hojat.smart.musicplayer.shared.ui.compose.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
internal object DynamicThemeRipple : RippleTheme {
    @Composable
    override fun defaultColor(): Color = if (isSurfaceLitWell()) ripple_light else LocalContentColor.current

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(pressedAlpha = 0.12f, focusedAlpha = 0.12f, draggedAlpha = 0.16f, hoveredAlpha = 0.08f)
}



