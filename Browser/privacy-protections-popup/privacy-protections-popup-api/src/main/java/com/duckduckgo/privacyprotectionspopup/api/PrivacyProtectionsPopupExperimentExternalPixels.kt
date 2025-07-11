

package com.duckduckgo.privacyprotectionspopup.api

interface PrivacyProtectionsPopupExperimentExternalPixels {
    /**
     * Returns parameters to annotate pixels with the popup experiment variant.
     */
    suspend fun getPixelParams(): Map<String, String>

    /**
     * This method should be invoked whenever the user enters the Privacy Dashboard screen.
     *
     * If the user is enrolled in the popup experiment, calling this method will fire a unique pixel.
     */
    fun tryReportPrivacyDashboardOpened()

    /**
     * This method should be invoked whenever the user toggles privacy protections on the Privacy Dashboard screen.
     *
     * If the user is enrolled in the popup experiment, calling this method will fire a unique pixel.
     */
    fun tryReportProtectionsToggledFromPrivacyDashboard(protectionsEnabled: Boolean)

    /**
     * This method should be invoked whenever the user toggles privacy protections using the options menu on the Browser screen.
     *
     * If the user is enrolled in the popup experiment, calling this method will fire a unique pixel.
     */
    fun tryReportProtectionsToggledFromBrowserMenu(protectionsEnabled: Boolean)

    /**
     * This method should be invoked whenever the user toggles privacy protections on the Broken Site screen.
     *
     * If the user is enrolled in the popup experiment, calling this method will fire a unique pixel.
     */
    fun tryReportProtectionsToggledFromBrokenSiteReport(protectionsEnabled: Boolean)
}
