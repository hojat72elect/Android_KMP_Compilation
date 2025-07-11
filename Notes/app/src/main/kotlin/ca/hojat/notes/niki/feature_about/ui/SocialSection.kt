package ca.hojat.notes.niki.feature_about.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.ui.compose.settings.SettingsGroup
import ca.hojat.notes.niki.shared.ui.compose.settings.SettingsHorizontalDivider
import ca.hojat.notes.niki.shared.ui.compose.settings.SettingsTitleTextComponent
import ca.hojat.notes.niki.shared.ui.compose.theme.SimpleTheme

private val startingTitlePadding = Modifier.padding(start = 60.dp)

@Composable
fun SocialSection(
    onFacebookClick: () -> Unit,
    onGithubClick: () -> Unit,
    onRedditClick: () -> Unit,
    onTelegramClick: () -> Unit
) {
    SettingsGroup(title = {
        SettingsTitleTextComponent(
            text = stringResource(id = R.string.social),
            modifier = startingTitlePadding
        )
    }) {
        SocialText(
            click = onFacebookClick,
            text = stringResource(id = R.string.facebook),
            icon = R.drawable.ic_facebook_vector,
        )
        SocialText(
            click = onGithubClick,
            text = stringResource(id = R.string.github),
            icon = R.drawable.ic_github_vector,
            tint = SimpleTheme.colorScheme.onSurface
        )
        SocialText(
            click = onRedditClick,
            text = stringResource(id = R.string.reddit),
            icon = R.drawable.ic_reddit_vector,
        )
        SocialText(
            click = onTelegramClick,
            text = stringResource(id = R.string.telegram),
            icon = R.drawable.ic_telegram_vector,
        )
        SettingsHorizontalDivider()
    }
}