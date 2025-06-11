

package com.duckduckgo.autofill.impl.ui.credential.management.suggestion

import android.content.Context
import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.impl.R.string
import com.duckduckgo.autofill.impl.ui.credential.management.sorting.CredentialListSorter
import com.duckduckgo.autofill.impl.ui.credential.management.viewing.list.ListItem
import com.duckduckgo.autofill.impl.ui.credential.management.viewing.list.ListItem.CredentialListItem.SuggestedCredential
import com.duckduckgo.autofill.impl.ui.credential.management.viewing.list.ListItem.Divider
import com.duckduckgo.autofill.impl.ui.credential.management.viewing.list.ListItem.GroupHeading
import javax.inject.Inject

class SuggestionListBuilder @Inject constructor(
    private val context: Context,
    private val sorter: CredentialListSorter,
) {

    fun build(
        unsortedDirectSuggestions: List<LoginCredentials>,
        unsortedSharableSuggestions: List<LoginCredentials>,
        allowBreakageReporting: Boolean,
    ): List<ListItem> {
        val list = mutableListOf<ListItem>()

        if (unsortedDirectSuggestions.isNotEmpty() || unsortedSharableSuggestions.isNotEmpty()) {
            list.add(GroupHeading(context.getString(string.credentialManagementSuggestionsLabel)))

            val sortedDirectSuggestions = sorter.sort(unsortedDirectSuggestions)
            val sortedSharableSuggestions = sorter.sort(unsortedSharableSuggestions)

            val allSuggestions = sortedDirectSuggestions + sortedSharableSuggestions
            list.addAll(allSuggestions.map { SuggestedCredential(it) })

            if (allowBreakageReporting) {
                list.add(ListItem.ReportAutofillBreakage)
            }

            list.add(Divider)
        }

        return list
    }
}
