

package com.duckduckgo.autofill.impl.importing

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface ImportedCredentialValidator {
    fun isValid(loginCredentials: GoogleCsvLoginCredential): Boolean
}

@ContributesBinding(AppScope::class)
class DefaultImportedCredentialValidator @Inject constructor() : ImportedCredentialValidator {

    override fun isValid(loginCredentials: GoogleCsvLoginCredential): Boolean {
        with(loginCredentials) {
            if (url?.startsWith(APP_PASSWORD_PREFIX) == true) {
                return false
            }

            if (allFieldsEmpty()) {
                return false
            }

            return true
        }
    }

    private fun GoogleCsvLoginCredential.allFieldsEmpty(): Boolean {
        return url.isNullOrBlank() &&
            username.isNullOrBlank() &&
            password.isNullOrBlank() &&
            title.isNullOrBlank() &&
            notes.isNullOrBlank()
    }

    companion object {
        private const val APP_PASSWORD_PREFIX = "android://"
    }
}
