

package com.duckduckgo.privacy.dashboard.impl.ui

import com.duckduckgo.app.global.model.Site
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.dashboard.impl.ui.PrivacyDashboardHybridViewModel.CookiePromptManagementState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AutoconsentStatusViewStateMapper {
    fun mapFromSite(site: Site): CookiePromptManagementState
}

@ContributesBinding(AppScope::class)
class CookiePromptManagementStatusViewStateMapper @Inject constructor() : AutoconsentStatusViewStateMapper {

    override fun mapFromSite(site: Site): CookiePromptManagementState {
        return CookiePromptManagementState(
            site.consentManaged,
            site.consentOptOutFailed,
            true,
            site.consentCosmeticHide,
        )
    }
}
