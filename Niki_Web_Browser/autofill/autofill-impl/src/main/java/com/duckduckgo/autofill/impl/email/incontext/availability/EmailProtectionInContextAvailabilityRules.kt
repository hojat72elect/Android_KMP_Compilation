

package com.duckduckgo.autofill.impl.email.incontext.availability

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.autofill.api.InternalTestUserChecker
import com.duckduckgo.autofill.impl.AutofillGlobalCapabilityChecker
import com.duckduckgo.autofill.impl.email.incontext.EmailProtectionInContextSignupFeature
import com.duckduckgo.autofill.impl.email.remoteconfig.EmailProtectionInContextExceptions
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface EmailProtectionInContextAvailabilityRules {
    suspend fun permittedToShow(url: String): Boolean
}

@ContributesBinding(AppScope::class)
class RealEmailProtectionInContextAvailabilityRules @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
    private val dispatchers: DispatcherProvider,
    private val emailProtectionInContextSignupFeature: EmailProtectionInContextSignupFeature,
    private val internalTestUserChecker: InternalTestUserChecker,
    private val exceptions: EmailProtectionInContextExceptions,
    private val autofillGlobalCapabilityChecker: AutofillGlobalCapabilityChecker,
    private val recentInstallChecker: EmailProtectionInContextRecentInstallChecker,
) : EmailProtectionInContextAvailabilityRules {

    override suspend fun permittedToShow(url: String): Boolean = withContext(dispatchers.io()) {
        if (!isFeatureEnabled()) return@withContext false
        if (!isEnglishLocale()) return@withContext false
        if (!withinInstallTimeLimit()) return@withContext false
        if (!isSecureAutofillAvailable()) return@withContext false
        if (!canShowForCurrentPage(url)) return@withContext false

        return@withContext true
    }

    private fun canShowForCurrentPage(url: String): Boolean {
        return !exceptions.isAnException(url)
    }

    private fun isFeatureEnabled(): Boolean {
        if (isInternalTester()) return true
        return emailProtectionInContextSignupFeature.self().isEnabled()
    }

    /**
     * Rule:in-context email protection sign up only available to the English locale
     *   - this is because the underlying web flow used is not localized to any other languages yet
     *   - if the web flow gets localization, we can remove this restriction
     */
    private fun isEnglishLocale(): Boolean {
        return appBuildConfig.deviceLocale.language == Locale("en").language
    }

    /**
     * Rule: only show in-context email protection sign up if the user installed recently
     */
    private suspend fun withinInstallTimeLimit(): Boolean {
        return recentInstallChecker.isRecentInstall()
    }

    private fun isInternalTester(): Boolean {
        return internalTestUserChecker.isInternalTestUser
    }

    private suspend fun isSecureAutofillAvailable() = autofillGlobalCapabilityChecker.isSecureAutofillAvailable()
}
