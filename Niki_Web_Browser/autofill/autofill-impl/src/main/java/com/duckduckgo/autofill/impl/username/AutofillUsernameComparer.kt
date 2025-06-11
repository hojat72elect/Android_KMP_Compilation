

package com.duckduckgo.autofill.impl.username

import com.duckduckgo.autofill.api.AutofillFeature
import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface AutofillUsernameComparer {
    suspend fun isEqual(
        username1: String?,
        username2: String?,
    ): Boolean

    suspend fun groupUsernamesAndPasswords(logins: List<LoginCredentials>): Map<Pair<String?, String?>, List<LoginCredentials>>
}

@ContributesBinding(AppScope::class)
class RealAutofillUsernameComparer @Inject constructor(
    private val autofillFeature: AutofillFeature,
    private val dispatchers: DispatcherProvider,
) : AutofillUsernameComparer {

    override suspend fun isEqual(username1: String?, username2: String?): Boolean {
        return withContext(dispatchers.io()) {
            if (username1 == null && username2 == null) return@withContext true
            if (username1 == null) return@withContext false
            if (username2 == null) return@withContext false

            username1.equals(username2, ignoreCase = autofillFeature.ignoreCaseOnUsernameComparisons().isEnabled())
        }
    }
    override suspend fun groupUsernamesAndPasswords(logins: List<LoginCredentials>): Map<Pair<String?, String?>, List<LoginCredentials>> {
        return if (autofillFeature.ignoreCaseOnUsernameComparisons().isEnabled()) {
            logins.groupBy { it.username?.lowercase() to it.password }
        } else {
            logins.groupBy { it.username to it.password }
        }
    }
}
