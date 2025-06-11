

package com.duckduckgo.privacyprotectionspopup.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacyprotectionspopup.api.PrivacyProtectionsPopupExperimentExternalPixels
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class PrivacyProtectionsPopupExperimentExternalPixelsImpl @Inject constructor() : PrivacyProtectionsPopupExperimentExternalPixels {

    override suspend fun getPixelParams(): Map<String, String> {
        return emptyMap()
    }

    override fun tryReportPrivacyDashboardOpened() {
    }

    override fun tryReportProtectionsToggledFromPrivacyDashboard(protectionsEnabled: Boolean) {
    }

    override fun tryReportProtectionsToggledFromBrowserMenu(protectionsEnabled: Boolean) {
    }

    override fun tryReportProtectionsToggledFromBrokenSiteReport(protectionsEnabled: Boolean) {
    }
}
