

package com.duckduckgo.privacy.config.impl.features.unprotectedtemporary

import com.duckduckgo.app.browser.UriString
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureException
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.duckduckgo.privacy.config.store.features.unprotectedtemporary.UnprotectedTemporaryRepository
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealUnprotectedTemporary @Inject constructor(private val repository: UnprotectedTemporaryRepository) : UnprotectedTemporary {

    override fun isAnException(url: String): Boolean {
        return matches(url)
    }

    override val unprotectedTemporaryExceptions: List<FeatureException>
        get() = repository.exceptions

    private fun matches(url: String): Boolean {
        return repository.exceptions.any { UriString.sameOrSubdomain(url, it.domain) }
    }
}
