

package com.duckduckgo.autofill.impl.ui

import com.duckduckgo.autofill.api.ExistingCredentialMatchDetector
import com.duckduckgo.autofill.api.ExistingCredentialMatchDetector.ContainsCredentialsResult
import com.duckduckgo.autofill.impl.store.InternalAutofillStore
import com.duckduckgo.common.utils.DefaultDispatcherProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

@ContributesBinding(AppScope::class)
class ExistingCredentialStoreInterrogatingMatchDetector @Inject constructor(
    private val autofillStore: InternalAutofillStore,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) :
    ExistingCredentialMatchDetector {

    override suspend fun determine(currentUrl: String, username: String?, password: String?): ContainsCredentialsResult {
        return withContext(dispatcherProvider.io()) {
            autofillStore.containsCredentials(currentUrl, username, password)
        }
    }
}
