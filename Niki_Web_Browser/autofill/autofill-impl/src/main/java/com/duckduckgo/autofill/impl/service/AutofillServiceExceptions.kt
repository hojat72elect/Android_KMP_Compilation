

package com.duckduckgo.autofill.impl.service

import com.duckduckgo.app.browser.UriString.Companion.sameOrSubdomain
import com.duckduckgo.autofill.impl.service.store.AutofillServiceFeatureRepository
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

interface AutofillServiceExceptions {
    fun isAnException(domain: String): Boolean
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealAutofillServiceExceptions @Inject constructor(
    private val repository: AutofillServiceFeatureRepository,
) : AutofillServiceExceptions {

    override fun isAnException(domain: String): Boolean {
        return repository.exceptions.any { sameOrSubdomain(domain, it) }
    }
}
