

package com.duckduckgo.autofill.impl.deduper

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AutofillLoginDeduplicator {

    suspend fun deduplicate(
        originalUrl: String,
        logins: List<LoginCredentials>,
    ): List<LoginCredentials>
}

@ContributesBinding(AppScope::class)
class RealAutofillLoginDeduplicator @Inject constructor(
    private val usernamePasswordMatcher: AutofillDeduplicationUsernameAndPasswordMatcher,
    private val bestMatchFinder: AutofillDeduplicationBestMatchFinder,
) : AutofillLoginDeduplicator {

    override suspend fun deduplicate(
        originalUrl: String,
        logins: List<LoginCredentials>,
    ): List<LoginCredentials> {
        val dedupedLogins = mutableListOf<LoginCredentials>()

        val groups = usernamePasswordMatcher.groupDuplicateCredentials(logins)
        groups.forEach {
            val bestMatchForGroup = bestMatchFinder.findBestMatch(originalUrl, it.value)
            if (bestMatchForGroup != null) {
                dedupedLogins.add(bestMatchForGroup)
            }
        }

        return dedupedLogins
    }
}
