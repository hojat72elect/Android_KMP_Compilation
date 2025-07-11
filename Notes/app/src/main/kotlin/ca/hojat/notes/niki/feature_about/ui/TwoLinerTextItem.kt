package ca.hojat.notes.niki.feature_about.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import ca.hojat.notes.niki.shared.ui.compose.settings.SettingsListItem
import ca.hojat.notes.niki.shared.ui.compose.theme.SimpleTheme


@Composable
fun TwoLinerTextItem(text: String, icon: Int, click: () -> Unit) {
    SettingsListItem(
        tint = SimpleTheme.colorScheme.onSurface,
        click = click,
        text = text,
        icon = icon,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}



