

package com.duckduckgo.autofill.impl.service

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.api.store.AutofillStore
import com.duckduckgo.autofill.impl.deduper.AutofillLoginDeduplicator
import com.duckduckgo.autofill.impl.service.mapper.AppCredentialProvider
import com.duckduckgo.autofill.impl.ui.credential.selecting.AutofillSelectCredentialsGrouper
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

interface AutofillSuggestions {
    suspend fun getSiteSuggestions(website: String): List<LoginCredentials>
    suspend fun getAppSuggestions(packageId: String): List<LoginCredentials>
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AutofillServiceSuggestions @Inject constructor(
    private val autofillStore: AutofillStore,
    private val loginDeduplicator: AutofillLoginDeduplicator,
    private val grouper: AutofillSelectCredentialsGrouper,
    private val appCredentialProvider: AppCredentialProvider,
    private val dispatcherProvider: DispatcherProvider,
) : AutofillSuggestions {

    override suspend fun getSiteSuggestions(website: String): List<LoginCredentials> {
        return withContext(dispatcherProvider.io()) {
            val credentials = autofillStore.getCredentials(website)
            loginDeduplicator.deduplicate(website, credentials).let { dedupLogins ->
                grouper.group(website, dedupLogins).let { groups ->
                    groups.perfectMatches
                        .plus(groups.partialMatches.values.flatten())
                        .plus(groups.shareableCredentials.values.flatten())
                }
            }.also {
                Timber.v("DDGAutofillService credentials for domain: $it")
            }
        }
    }

    override suspend fun getAppSuggestions(packageId: String): List<LoginCredentials> {
        return withContext(dispatcherProvider.io()) {
            appCredentialProvider.getCredentials(packageId).also {
                Timber.v("DDGAutofillService credentials for package: $it")
            }
        }
    }
}
