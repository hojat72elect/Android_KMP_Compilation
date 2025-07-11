

package com.duckduckgo.privacyprotectionspopup.impl

import com.duckduckgo.app.privacy.db.UserAllowListRepository
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.feature.toggles.api.FeatureToggle
import com.duckduckgo.privacy.config.api.ContentBlocking
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface ProtectionsStateProvider {
    fun areProtectionsEnabled(domain: String): Flow<Boolean>
}

@ContributesBinding(FragmentScope::class)
class ProtectionsStateProviderImpl @Inject constructor(
    private val featureToggle: FeatureToggle,
    private val contentBlocking: ContentBlocking,
    private val unprotectedTemporary: UnprotectedTemporary,
    private val userAllowListRepository: UserAllowListRepository,
) : ProtectionsStateProvider {

    override fun areProtectionsEnabled(domain: String): Flow<Boolean> {
        if (
            !featureToggle.isFeatureEnabled(PrivacyFeatureName.ContentBlockingFeatureName.value) ||
            contentBlocking.isAnException(domain) ||
            unprotectedTemporary.isAnException(domain)
        ) {
            return flowOf(false)
        }

        return userAllowListRepository.domainsInUserAllowListFlow()
            .map { allowlistedDomains -> domain !in allowlistedDomains }
            .distinctUntilChanged()
    }
}
