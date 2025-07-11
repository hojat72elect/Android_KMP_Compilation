package ca.hojat.notes.niki.feature_about.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.ui.compose.lists.SimpleColumnScaffold
import ca.hojat.notes.niki.shared.ui.compose.settings.SettingsListItem


@Composable
fun AboutScreen(
    goBack: () -> Unit,
    helpUsSection: @Composable () -> Unit,
    aboutSection: @Composable () -> Unit,
    socialSection: @Composable () -> Unit,
    otherSection: @Composable () -> Unit,
) {
    SimpleColumnScaffold(title = stringResource(id = R.string.about), goBack = goBack) {
        aboutSection()
        helpUsSection()
        socialSection()
        otherSection()
        SettingsListItem(text = stringResource(id = R.string.about_footer))
    }
}

