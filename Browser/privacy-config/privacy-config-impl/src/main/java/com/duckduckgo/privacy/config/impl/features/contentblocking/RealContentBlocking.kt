

package com.duckduckgo.privacy.config.impl.features.contentblocking

import com.duckduckgo.app.browser.UriString.Companion.sameOrSubdomain
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureToggle
import com.duckduckgo.privacy.config.api.ContentBlocking
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.duckduckgo.privacy.config.store.features.contentblocking.ContentBlockingRepository
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealContentBlocking @Inject constructor(
    private val contentBlockingRepository: ContentBlockingRepository,
    private val featureToggle: FeatureToggle,
    private val unprotectedTemporary: UnprotectedTemporary,
) : ContentBlocking {

    override fun isAnException(url: String): Boolean {
        return if (featureToggle.isFeatureEnabled(PrivacyFeatureName.ContentBlockingFeatureName.value, true)) {
            unprotectedTemporary.isAnException(url) || matches(url)
        } else {
            false
        }
    }

    private fun matches(url: String): Boolean {
        return contentBlockingRepository.exceptions.any { sameOrSubdomain(url, it.domain) }
    }
}
