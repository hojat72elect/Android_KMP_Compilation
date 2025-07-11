

package com.duckduckgo.autofill.impl

import com.duckduckgo.app.browser.UriString.Companion.sameOrSubdomain
import com.duckduckgo.autofill.api.Autofill
import com.duckduckgo.autofill.impl.feature.plugin.AutofillFeatureRepository
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealAutofill @Inject constructor(
    private val autofillRepository: AutofillFeatureRepository,
    private val unprotectedTemporary: UnprotectedTemporary,
) : Autofill {

    override fun isAnException(url: String): Boolean {
        return unprotectedTemporary.isAnException(url) || matches(url)
    }

    private fun matches(url: String): Boolean {
        return autofillRepository.exceptions.any { sameOrSubdomain(url, it.domain) }
    }
}
