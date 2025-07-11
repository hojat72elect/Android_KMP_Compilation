

package com.duckduckgo.autofill.impl

import com.duckduckgo.autofill.api.AutofillCapabilityChecker
import com.duckduckgo.autofill.api.AutofillFeature
import com.duckduckgo.autofill.api.InternalTestUserChecker
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

@ContributesBinding(AppScope::class)
class AutofillCapabilityCheckerImpl @Inject constructor(
    private val autofillFeature: AutofillFeature,
    private val internalTestUserChecker: InternalTestUserChecker,
    private val autofillGlobalCapabilityChecker: AutofillGlobalCapabilityChecker,
    private val dispatcherProvider: DispatcherProvider,
) : AutofillCapabilityChecker {

    override suspend fun canInjectCredentialsToWebView(url: String): Boolean = withContext(dispatcherProvider.io()) {
        if (!isSecureAutofillAvailable()) return@withContext false
        if (!isAutofillEnabledByConfiguration(url)) return@withContext false
        if (!isAutofillEnabledByUser()) return@withContext false

        if (isInternalTester()) return@withContext true

        return@withContext autofillFeature.canInjectCredentials().isEnabled()
    }

    override suspend fun canSaveCredentialsFromWebView(url: String): Boolean = withContext(dispatcherProvider.io()) {
        if (!isSecureAutofillAvailable()) return@withContext false
        if (!isAutofillEnabledByConfiguration(url)) return@withContext false
        if (!isAutofillEnabledByUser()) return@withContext false

        if (isInternalTester()) return@withContext true

        return@withContext autofillFeature.canSaveCredentials().isEnabled()
    }

    override suspend fun canGeneratePasswordFromWebView(url: String): Boolean = withContext(dispatcherProvider.io()) {
        if (!isSecureAutofillAvailable()) return@withContext false
        if (!isAutofillEnabledByConfiguration(url)) return@withContext false
        if (!isAutofillEnabledByUser()) return@withContext false

        if (isInternalTester()) return@withContext true

        return@withContext autofillFeature.canGeneratePasswords().isEnabled()
    }

    /**
     * Because the credential management screen handles the states where the user has toggled autofill off, or the device can't support it,
     * this feature is not dependent those checks.
     *
     * We purposely don't couple this check against [isSecureAutofillAvailable] or [isAutofillEnabledByUser].
     */
    override suspend fun canAccessCredentialManagementScreen(): Boolean = withContext(dispatcherProvider.io()) {
        if (isInternalTester()) return@withContext true
        if (!isGlobalFeatureEnabled()) return@withContext false
        return@withContext autofillFeature.canAccessCredentialManagement().isEnabled()
    }

    private suspend fun isInternalTester(): Boolean {
        return withContext(dispatcherProvider.io()) {
            internalTestUserChecker.isInternalTestUser
        }
    }

    private suspend fun isGlobalFeatureEnabled(): Boolean {
        return withContext(dispatcherProvider.io()) {
            autofillFeature.self().isEnabled()
        }
    }

    override suspend fun isAutofillEnabledByConfiguration(url: String) = autofillGlobalCapabilityChecker.isAutofillEnabledByConfiguration(url)

    private suspend fun isSecureAutofillAvailable() = autofillGlobalCapabilityChecker.isSecureAutofillAvailable()

    private suspend fun isAutofillEnabledByUser() = autofillGlobalCapabilityChecker.isAutofillEnabledByUser()
}
