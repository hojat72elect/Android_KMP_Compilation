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

private val startingTitlePadding = Modifier.padding(start = 60.dp)

@Composable
fun AboutSection(
    setupFAQ: Boolean,
    onFAQClick: () -> Unit,
    onEmailClick: () -> Unit
) {
    SettingsGroup(title = {
        SettingsTitleTextComponent(
            text = stringResource(id = R.string.support),
            modifier = startingTitlePadding
        )
    }) {
        if (setupFAQ) {
            TwoLinerTextItem(
                click = onFAQClick,
                text = stringResource(id = R.string.frequently_asked_questions),
                icon = R.drawable.ic_question_mark_vector
            )
        }
        TwoLinerTextItem(
            click = onEmailClick,
            text = stringResource(id = R.string.my_email),
            icon = R.drawable.ic_mail_vector
        )
        SettingsHorizontalDivider()
    }
}
