

package com.duckduckgo.autofill.impl.deduper

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.impl.username.AutofillUsernameComparer
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AutofillDeduplicationUsernameAndPasswordMatcher {

    suspend fun groupDuplicateCredentials(logins: List<LoginCredentials>): Map<Pair<String?, String?>, List<LoginCredentials>>
}

@ContributesBinding(AppScope::class)
class RealAutofillDeduplicationUsernameAndPasswordMatcher @Inject constructor(
    private val usernameComparer: AutofillUsernameComparer,
) : AutofillDeduplicationUsernameAndPasswordMatcher {

    override suspend fun groupDuplicateCredentials(logins: List<LoginCredentials>): Map<Pair<String?, String?>, List<LoginCredentials>> {
        return usernameComparer.groupUsernamesAndPasswords(logins)
    }
}
