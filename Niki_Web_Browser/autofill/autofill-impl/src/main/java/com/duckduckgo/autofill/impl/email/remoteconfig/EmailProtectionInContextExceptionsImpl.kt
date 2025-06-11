

package com.duckduckgo.autofill.impl.email.remoteconfig

import com.duckduckgo.app.browser.UriString.Companion.sameOrSubdomain
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

interface EmailProtectionInContextExceptions {
    fun isAnException(url: String): Boolean
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class EmailProtectionInContextExceptionsImpl @Inject constructor(
    private val repository: EmailProtectionInContextFeatureRepository,
) : EmailProtectionInContextExceptions {

    override fun isAnException(url: String): Boolean {
        return repository.exceptions.any { sameOrSubdomain(url, it) }
    }
}
