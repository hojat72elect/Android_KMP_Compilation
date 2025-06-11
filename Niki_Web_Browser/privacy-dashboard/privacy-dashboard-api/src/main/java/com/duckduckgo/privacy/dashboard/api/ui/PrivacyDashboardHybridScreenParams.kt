

package com.duckduckgo.privacy.dashboard.api.ui

import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.privacy.dashboard.api.ui.DashboardOpener.NONE

enum class DashboardOpener(val value: String) {
    MENU("menu"),
    DASHBOARD("dashboard"),
    RELOAD_THREE_TIMES_WITHIN_20_SECONDS("reload-three-times-within-20-seconds"),
    NONE(""),
}

sealed class PrivacyDashboardHybridScreenParams : GlobalActivityStarter.ActivityParams {

    abstract val tabId: String
    abstract val opener: DashboardOpener

    /**
     * Use this parameter to launch the privacy dashboard hybrid activity with the given tabId
     * @param tabId The tab ID
     */
    data class PrivacyDashboardPrimaryScreen(
        override val tabId: String,
        override val opener: DashboardOpener = NONE,
    ) : PrivacyDashboardHybridScreenParams()

    /**
     * Use this parameter to launch the site breakage reporting form.
     * @param tabId The tab ID
     */
    data class BrokenSiteForm(
        override val tabId: String,
        override val opener: DashboardOpener = NONE,
        val reportFlow: BrokenSiteFormReportFlow,
    ) : PrivacyDashboardHybridScreenParams() {
        enum class BrokenSiteFormReportFlow {
            MENU,
            RELOAD_THREE_TIMES_WITHIN_20_SECONDS,
        }
    }

    /**
     * Use this parameter to launch the toggle report form.
     * @param tabId The tab ID
     */
    data class PrivacyDashboardToggleReportScreen(
        override val tabId: String,
        override val opener: DashboardOpener = NONE,
    ) : PrivacyDashboardHybridScreenParams()
}

object PrivacyDashboardHybridScreenResult {
    const val REPORT_SUBMITTED = 1
}
