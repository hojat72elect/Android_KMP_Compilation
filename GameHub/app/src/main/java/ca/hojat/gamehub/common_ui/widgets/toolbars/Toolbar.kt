package ca.hojat.gamehub.common_ui.widgets.toolbars

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ca.hojat.gamehub.R
import ca.hojat.gamehub.common_ui.theme.GameHubTheme

@Composable
fun Toolbar(
    title: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    backgroundColor: Color = GameHubTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = ToolbarElevation,
    titleTextStyle: TextStyle = GameHubTheme.typography.h5,
    backButtonIcon: Painter? = null,
    firstButtonIcon: Painter? = null,
    secondButtonIcon: Painter? = null,
    onBackButtonClick: (() -> Unit)? = null,
    onFirstButtonClick: (() -> Unit)? = null,
    onSecondButtonClick: (() -> Unit)? = null,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .height(ToolbarHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // the padding on left and right of the title of the Toolbar
            val titleLeftPadding = getTitleHorizontalPadding(backButtonIcon)
            val titleRightPadding = getTitleHorizontalPadding(firstButtonIcon)

            if (backButtonIcon != null) {
                Button(
                    icon = backButtonIcon,
                    onClick = { onBackButtonClick?.invoke() }
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = titleLeftPadding, end = titleRightPadding),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = titleTextStyle,
            )

            // We're drawing this row from left to right (unless the locale is RTL)
            // so, the second button should come before the first button.

            if (secondButtonIcon != null) {
                Button(
                    icon = secondButtonIcon,
                    onClick = { onSecondButtonClick?.invoke() }
                )
            }

            if (firstButtonIcon != null) {
                Button(
                    icon = firstButtonIcon,
                    onClick = { onFirstButtonClick?.invoke() }
                )
            }
        }
    }
}

@Composable
private fun getTitleHorizontalPadding(icon: Painter?): Dp {
    return if (icon != null) {
        GameHubTheme.spaces.spacing_4_0
    } else {
        GameHubTheme.spaces.spacing_5_0
    }
}

@Composable
private fun Button(
    icon: Painter,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.size(ToolbarHeight),
        onClick = onClick,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ToolbarPreviewWithTitle() {
    GameHubTheme {
        Toolbar(
            title = "Toolbar"
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ToolbarPreviewWithLongTitle() {
    GameHubTheme {
        Toolbar(
            title = "Toolbar toolbar toolbar toolbar toolbar toolbar toolbar toolbar"
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ToolbarPreviewWithBothIcons() {
    GameHubTheme {
        Toolbar(
            title = "Toolbar",
            backButtonIcon = painterResource(R.drawable.arrow_left),
            firstButtonIcon = painterResource(R.drawable.magnify)
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ToolbarPreviewWithLeftIcon() {
    GameHubTheme {
        Toolbar(
            title = "Toolbar",
            backButtonIcon = painterResource(R.drawable.arrow_left),
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ToolbarPreviewWithRightIcon() {
    GameHubTheme {
        Toolbar(
            title = "Toolbar",
            firstButtonIcon = painterResource(R.drawable.magnify)
        )
    }
}
