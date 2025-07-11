

package com.duckduckgo.autofill.impl.deduper

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.impl.deduper.AutofillDeduplicationMatchTypeDetector.MatchType
import com.duckduckgo.autofill.impl.urlmatcher.AutofillUrlMatcher
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AutofillDeduplicationMatchTypeDetector {

    fun detectMatchType(
        originalUrl: String,
        login: LoginCredentials,
    ): MatchType

    sealed interface MatchType {
        object PerfectMatch : MatchType
        object PartialMatch : MatchType
        object NotAMatch : MatchType
    }
}

@ContributesBinding(AppScope::class)
class RealAutofillDeduplicationMatchTypeDetector @Inject constructor(
    private val urlMatcher: AutofillUrlMatcher,
) : AutofillDeduplicationMatchTypeDetector {

    override fun detectMatchType(
        originalUrl: String,
        login: LoginCredentials,
    ): MatchType {
        val visitedSiteParts = urlMatcher.extractUrlPartsForAutofill(originalUrl)
        val savedSiteParts = urlMatcher.extractUrlPartsForAutofill(login.domain)

        if (!urlMatcher.matchingForAutofill(visitedSiteParts, savedSiteParts)) {
            return MatchType.NotAMatch
        }

        return if (visitedSiteParts.subdomain == savedSiteParts.subdomain) {
            MatchType.PerfectMatch
        } else {
            MatchType.PartialMatch
        }
    }
}
