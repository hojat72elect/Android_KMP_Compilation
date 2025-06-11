

package com.duckduckgo.autofill.impl.importing.gpm.webflow.autofill

import com.duckduckgo.autofill.api.AutofillEventListener
import com.duckduckgo.autofill.api.Callback
import com.duckduckgo.autofill.api.EmailProtectionInContextSignupFlowListener
import com.duckduckgo.autofill.api.EmailProtectionUserPromptListener
import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.api.domain.app.LoginTriggerType

interface NoOpAutofillCallback : Callback {
    override suspend fun onCredentialsAvailableToInject(
        originalUrl: String,
        credentials: List<LoginCredentials>,
        triggerType: LoginTriggerType,
    ) {
    }

    override suspend fun onCredentialsAvailableToSave(
        currentUrl: String,
        credentials: LoginCredentials,
    ) {
    }

    override suspend fun onGeneratedPasswordAvailableToUse(
        originalUrl: String,
        username: String?,
        generatedPassword: String,
    ) {
    }

    override fun noCredentialsAvailable(originalUrl: String) {
    }

    override fun onCredentialsSaved(savedCredentials: LoginCredentials) {
    }
}

interface NoOpAutofillEventListener : AutofillEventListener {
    override fun onAcceptGeneratedPassword(originalUrl: String) {
    }

    override fun onRejectGeneratedPassword(originalUrl: String) {
    }

    override fun onUseEmailProtectionPersonalAddress(
        originalUrl: String,
        duckAddress: String,
    ) {
    }

    override fun onUseEmailProtectionPrivateAlias(
        originalUrl: String,
        duckAddress: String,
    ) {
    }

    override fun onSelectedToSignUpForInContextEmailProtection() {
    }

    override fun onEndOfEmailProtectionInContextSignupFlow() {
    }

    override fun onShareCredentialsForAutofill(
        originalUrl: String,
        selectedCredentials: LoginCredentials,
    ) {
    }

    override fun onNoCredentialsChosenForAutofill(originalUrl: String) {
    }

    override fun onSavedCredentials(credentials: LoginCredentials) {
    }

    override fun onUpdatedCredentials(credentials: LoginCredentials) {
    }

    override fun onAutofillStateChange() {
    }
}

interface NoOpEmailProtectionInContextSignupFlowListener : EmailProtectionInContextSignupFlowListener {
    override fun closeInContextSignup() {
    }
}

interface NoOpEmailProtectionUserPromptListener : EmailProtectionUserPromptListener {
    override fun showNativeInContextEmailProtectionSignupPrompt() {
    }

    override fun showNativeChooseEmailAddressPrompt() {
    }
}
