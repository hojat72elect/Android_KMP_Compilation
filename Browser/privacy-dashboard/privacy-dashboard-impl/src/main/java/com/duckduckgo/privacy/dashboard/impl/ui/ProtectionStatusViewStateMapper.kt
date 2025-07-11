

package com.duckduckgo.privacy.dashboard.impl.ui

import com.duckduckgo.app.global.model.Site
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.ContentBlocking
import com.duckduckgo.privacy.config.api.PrivacyFeatureName.ContentBlockingFeatureName
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.duckduckgo.privacy.dashboard.impl.ui.PrivacyDashboardHybridViewModel.ProtectionStatusViewState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface ProtectionStatusViewStateMapper {
    fun mapFromSite(site: Site): ProtectionStatusViewState
}

@ContributesBinding(AppScope::class)
class AppProtectionStatusViewStateMapper @Inject constructor(
    private val contentBlocking: ContentBlocking,
    private val unprotectedTemporary: UnprotectedTemporary,
) : ProtectionStatusViewStateMapper {

    override fun mapFromSite(site: Site): ProtectionStatusViewState {
        // List of enabled features that are supported by the privacy dashboard
        // docs: https://duckduckgo.github.io/privacy-dashboard/example/docs/interfaces/Generated_Schema_Definitions.ProtectionsStatus.html#enabledFeatures
        // if too many privacy features are required as dependencies, extract them via plugins
        val enabledFeatures = mutableListOf<String>().apply {
            if (!contentBlocking.isAnException(site.url)) {
                add(ContentBlockingFeatureName.value)
            }
        }

        return ProtectionStatusViewState(
            allowlisted = site.userAllowList,
            denylisted = false,
            enabledFeatures = enabledFeatures,
            unprotectedTemporary = unprotectedTemporary.isAnException(site.url),
        )
    }
}
