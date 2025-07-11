

package com.duckduckgo.privacy.dashboard.impl.ui

import android.webkit.JavascriptInterface

class PrivacyDashboardJavascriptInterface constructor(
    val onPrivacyProtectionsClicked: (String) -> Unit,
    val onUrlClicked: (String) -> Unit,
    val onOpenSettings: (String) -> Unit,
    val onClose: () -> Unit,
    val onSubmitBrokenSiteReport: (String) -> Unit,
    val onGetToggleReportOptions: () -> Unit,
    val onSendToggleReport: () -> Unit,
    val onRejectToggleReport: () -> Unit,
    val onSeeWhatIsSent: () -> Unit,
    val onShowNativeFeedback: () -> Unit,
    val onReportBrokenSiteShown: () -> Unit,
) {
    @JavascriptInterface
    fun toggleAllowlist(payload: String) {
        onPrivacyProtectionsClicked(payload)
    }

    @JavascriptInterface
    fun close() {
        onClose()
    }

    @JavascriptInterface
    fun showBreakageForm() {
        // FE handles navigation internally, but we must keep this callback for it to work.
    }

    @JavascriptInterface
    fun openInNewTab(payload: String) {
        onUrlClicked(payload)
    }

    @JavascriptInterface
    fun openSettings(payload: String) {
        onOpenSettings(payload)
    }

    @JavascriptInterface
    fun submitBrokenSiteReport(payload: String) {
        onSubmitBrokenSiteReport(payload)
    }

    @JavascriptInterface
    fun getToggleReportOptions() {
        onGetToggleReportOptions()
    }

    @JavascriptInterface
    fun sendToggleReport() {
        onSendToggleReport()
    }

    @JavascriptInterface
    fun rejectToggleReport() {
        onRejectToggleReport()
    }

    @JavascriptInterface
    fun seeWhatIsSent() {
        onSeeWhatIsSent()
    }

    @JavascriptInterface
    fun showNativeFeedback() {
        onShowNativeFeedback()
    }

    @JavascriptInterface
    fun reportBrokenSiteShown() {
        onReportBrokenSiteShown()
    }

    companion object {
        // Interface name used inside js layer
        const val JAVASCRIPT_INTERFACE_NAME = "PrivacyDashboard"
    }
}
