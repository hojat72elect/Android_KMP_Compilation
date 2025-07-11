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
 fun HelpUsSection(
    onRateUsClick: () -> Unit,
    onInviteClick: () -> Unit,
    onContributorsClick: () -> Unit,
    showRateUs: Boolean,
    showInvite: Boolean,
    showDonate: Boolean,
    onDonateClick: () -> Unit,
) {
    SettingsGroup(title = {
        SettingsTitleTextComponent(
            text = stringResource(id = R.string.help_us),
            modifier = startingTitlePadding
        )
    }) {
        if (showRateUs) {
            TwoLinerTextItem(
                text = stringResource(id = R.string.rate_us),
                icon = R.drawable.ic_star_vector,
                click = onRateUsClick
            )
        }
        if (showInvite) {
            TwoLinerTextItem(
                text = stringResource(id = R.string.invite_friends),
                icon = R.drawable.ic_add_person_vector,
                click = onInviteClick
            )
        }
        TwoLinerTextItem(
            click = onContributorsClick,
            text = stringResource(id = R.string.contributors),
            icon = R.drawable.ic_face_vector
        )
        if (showDonate) {
            TwoLinerTextItem(
                click = onDonateClick,
                text = stringResource(id = R.string.donate),
                icon = R.drawable.ic_dollar_vector
            )
        }
        SettingsHorizontalDivider()
    }
}