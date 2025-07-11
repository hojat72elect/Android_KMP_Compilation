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
fun OtherSection(
    showMoreApps: Boolean,
    onMoreAppsClick: () -> Unit,
    onWebsiteClick: () -> Unit,
    showWebsite: Boolean,
    showPrivacyPolicy: Boolean,
    onPrivacyPolicyClick: () -> Unit,
    onLicenseClick: () -> Unit,
    version: String,
    onVersionClick: () -> Unit,
) {
    SettingsGroup(title = {
        SettingsTitleTextComponent(
            text = stringResource(id = R.string.other),
            modifier = startingTitlePadding
        )
    }) {
        if (showMoreApps) {
            TwoLinerTextItem(
                click = onMoreAppsClick,
                text = stringResource(id = R.string.more_apps_from_us),
                icon = R.drawable.ic_heart_vector
            )
        }
        if (showWebsite) {
            TwoLinerTextItem(
                click = onWebsiteClick,
                text = stringResource(id = R.string.website),
                icon = R.drawable.ic_link_vector
            )
        }
        if (showPrivacyPolicy) {
            TwoLinerTextItem(
                click = onPrivacyPolicyClick,
                text = stringResource(id = R.string.privacy_policy),
                icon = R.drawable.ic_unhide_vector
            )
        }
        TwoLinerTextItem(
            click = onLicenseClick,
            text = stringResource(id = R.string.third_party_licences),
            icon = R.drawable.ic_article_vector
        )
        TwoLinerTextItem(
            click = onVersionClick,
            text = version,
            icon = R.drawable.ic_info_vector
        )
        SettingsHorizontalDivider()
    }
}