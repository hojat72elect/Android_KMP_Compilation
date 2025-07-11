

package com.duckduckgo.autofill.impl.email.incontext.availability

import com.duckduckgo.autofill.impl.email.incontext.store.EmailProtectionInContextDataStore
import com.duckduckgo.browser.api.UserBrowserProperties
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface EmailProtectionInContextRecentInstallChecker {
    suspend fun isRecentInstall(): Boolean
}

@ContributesBinding(AppScope::class)
class RealEmailProtectionInContextRecentInstallChecker @Inject constructor(
    private val userBrowserProperties: UserBrowserProperties,
    private val dataStore: EmailProtectionInContextDataStore,
    private val dispatchers: DispatcherProvider,
) : EmailProtectionInContextRecentInstallChecker {

    override suspend fun isRecentInstall(): Boolean = withContext(dispatchers.io()) {
        val maxInstalledDays = dataStore.getMaximumPermittedDaysSinceInstallation()
        val daysSinceInstall = userBrowserProperties.daysSinceInstalled()

        return@withContext daysSinceInstall <= maxInstalledDays
    }
}
