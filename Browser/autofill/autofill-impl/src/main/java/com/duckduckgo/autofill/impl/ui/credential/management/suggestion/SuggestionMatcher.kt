

package com.duckduckgo.autofill.impl.ui.credential.management.suggestion

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.impl.sharedcreds.ShareableCredentials
import com.duckduckgo.autofill.impl.urlmatcher.AutofillUrlMatcher
import javax.inject.Inject

class SuggestionMatcher @Inject constructor(
    private val autofillUrlMatcher: AutofillUrlMatcher,
    private val shareableCredentials: ShareableCredentials,
) {

    /**
     * Returns a list of credentials that are a direct match for the current URL.
     * By direct, this means it does not consider sharable credentials. @see [getShareableSuggestions] for those.
     */
    fun getDirectSuggestions(
        currentUrl: String?,
        credentials: List<LoginCredentials>,
    ): List<LoginCredentials> {
        if (currentUrl == null) return emptyList()
        val currentSite = autofillUrlMatcher.extractUrlPartsForAutofill(currentUrl)
        if (currentSite.eTldPlus1 == null) return emptyList()

        return credentials.filter {
            val storedDomain = it.domain ?: return@filter false
            val savedSite = autofillUrlMatcher.extractUrlPartsForAutofill(storedDomain)
            return@filter autofillUrlMatcher.matchingForAutofill(currentSite, savedSite)
        }
    }

    /**
     * Returns a list of credentials that are not a direct match for the current URL, but are considered shareable.
     */
    suspend fun getShareableSuggestions(currentUrl: String?): List<LoginCredentials> {
        if (currentUrl == null) return emptyList()
        return shareableCredentials.shareableCredentials(currentUrl)
    }
}
