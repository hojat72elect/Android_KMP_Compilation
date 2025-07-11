

package com.duckduckgo.autofill.impl.ui.credential.management.viewing.list

import android.view.View
import com.duckduckgo.autofill.api.domain.app.LoginCredentials

sealed interface ListItem {
    sealed class CredentialListItem(open val credentials: LoginCredentials) : ListItem {
        data class Credential(override val credentials: LoginCredentials) : CredentialListItem(credentials)
        data class SuggestedCredential(override val credentials: LoginCredentials) : CredentialListItem(credentials)
    }

    data object ReportAutofillBreakage : ListItem
    data class GroupHeading(val label: String) : ListItem
    data object Divider : ListItem
    data class NoMatchingSearchResults(val query: String) : ListItem
    data class TopLevelControls(val initialToggleStateIsEnabled: Boolean) : ListItem
    data class PromotionContainer(val promotionView: View) : ListItem
    data class EmptyStateView(val showGoogleImportButton: Boolean) : ListItem
}
