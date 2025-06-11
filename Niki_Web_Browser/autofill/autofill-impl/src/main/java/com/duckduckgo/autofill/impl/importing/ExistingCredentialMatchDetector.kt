

package com.duckduckgo.autofill.impl.importing

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.impl.store.InternalAutofillStore
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

interface ExistingCredentialMatchDetector {
    suspend fun filterExistingCredentials(newCredentials: List<LoginCredentials>): List<LoginCredentials>
}

@ContributesBinding(AppScope::class)
class DefaultExistingCredentialMatchDetector @Inject constructor(
    private val autofillStore: InternalAutofillStore,
    private val dispatchers: DispatcherProvider,
) : ExistingCredentialMatchDetector {

    override suspend fun filterExistingCredentials(newCredentials: List<LoginCredentials>): List<LoginCredentials> {
        return withContext(dispatchers.io()) {
            val existingCredentials = autofillStore.getAllCredentials().firstOrNull() ?: return@withContext newCredentials

            // Filter new credentials to exclude those already in the database
            newCredentials.filter { newCredential ->

                existingCredentials.none { existingCredential ->
                    existingCredential.domain == newCredential.domain &&
                        existingCredential.username == newCredential.username &&
                        existingCredential.password == newCredential.password &&
                        existingCredential.domainTitle == newCredential.domainTitle &&
                        existingCredential.notes == newCredential.notes
                }
            }
        }
    }
}
