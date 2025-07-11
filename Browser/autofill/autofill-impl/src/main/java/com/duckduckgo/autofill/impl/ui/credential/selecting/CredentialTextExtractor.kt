

package com.duckduckgo.autofill.impl.ui.credential.selecting

import android.content.Context
import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.autofill.impl.R
import com.duckduckgo.common.utils.extractDomain

class CredentialTextExtractor(private val applicationContext: Context) {

    fun usernameOrPlaceholder(credentials: LoginCredentials): String {
        credentials.username.let { username ->
            return if (username.isNullOrBlank()) {
                buildPlaceholderForSite(credentials.domain)
            } else {
                username
            }
        }
    }

    private fun buildPlaceholderForSite(domain: String?): String {
        return if (domain.isNullOrBlank()) {
            missingUsernameAndSitePlaceholder()
        } else {
            val domainName = domain.extractDomain() ?: return missingUsernameAndSitePlaceholder()
            applicationContext.getString(R.string.useSavedLoginDialogMissingUsernameButtonText, domainName)
        }
    }

    private fun missingUsernameAndSitePlaceholder(): String {
        return applicationContext.getString(R.string.useSavedLoginDialogMissingUsernameMissingDomainButtonText)
    }
}
