package ca.hojat.gamehub.common_ui.widgets

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import ca.hojat.gamehub.common_ui.theme.GameHubTheme

@Composable
fun GameNewsProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = GameHubTheme.colors.secondary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
        strokeWidth = strokeWidth,
    )
}
