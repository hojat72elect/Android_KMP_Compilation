

package com.duckduckgo.autofill.impl.service.mapper

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.api.store.AutofillStore
import com.duckduckgo.autofill.impl.service.AutofillServiceFeature
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

interface AppCredentialProvider {
    /**
     * Provide a list of unique credentials that can be associated with the given [appPackage]
     */
    suspend fun getCredentials(appPackage: String): List<LoginCredentials>
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealAppCredentialProvider @Inject constructor(
    private val appToDomainMapper: AppToDomainMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val autofillStore: AutofillStore,
    private val autofillServiceFeature: AutofillServiceFeature,
) : AppCredentialProvider {
    override suspend fun getCredentials(appPackage: String): List<LoginCredentials> = withContext(dispatcherProvider.io()) {
        if (autofillServiceFeature.canMapAppToDomain().isEnabled().not()) return@withContext emptyList()

        Timber.d("Autofill-mapping: Getting credentials for $appPackage")
        return@withContext appToDomainMapper.getAssociatedDomains(appPackage).map {
            getAllCredentialsFromDomain(it)
        }.flatten().distinct().also {
            Timber.d("Autofill-mapping: Total credentials for $appPackage : ${it.size}")
        }
    }

    private suspend fun getAllCredentialsFromDomain(domain: String): List<LoginCredentials> {
        return autofillStore.getCredentials(domain)
    }
}
