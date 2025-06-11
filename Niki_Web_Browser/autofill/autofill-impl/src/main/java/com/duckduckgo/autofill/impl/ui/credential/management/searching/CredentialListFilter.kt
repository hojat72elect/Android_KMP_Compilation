

package com.duckduckgo.autofill.impl.ui.credential.management.searching

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface CredentialListFilter {
    suspend fun filter(
        originalList: List<LoginCredentials>,
        query: String,
    ): List<LoginCredentials>
}

@ContributesBinding(AppScope::class)
class ManagementScreenCredentialListFilter @Inject constructor(private val autofillCredentialMatcher: AutofillCredentialMatcher) :
    CredentialListFilter {

    override suspend fun filter(
        originalList: List<LoginCredentials>,
        query: String,
    ): List<LoginCredentials> {
        if (query.isBlank()) return originalList

        return originalList.filter {
            autofillCredentialMatcher.matches(it, query)
        }
    }
}

interface AutofillCredentialMatcher {
    fun matches(
        credential: LoginCredentials,
        query: String,
    ): Boolean
}

@ContributesBinding(AppScope::class)
class ManagementScreenAutofillCredentialMatcher @Inject constructor() : AutofillCredentialMatcher {
    override fun matches(
        credential: LoginCredentials,
        query: String,
    ): Boolean {
        if (query.isBlank()) return true
        var matches = false

        if (credential.username?.contains(query, true) == true) {
            matches = true
        } else if (credential.domainTitle?.contains(query, true) == true) {
            matches = true
        } else if (credential.notes?.contains(query, true) == true) {
            matches = true
        } else if (credential.domain?.contains(query, true) == true) {
            matches = true
        }

        return matches
    }
}
