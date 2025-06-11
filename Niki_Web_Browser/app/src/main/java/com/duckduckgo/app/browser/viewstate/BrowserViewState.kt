

package com.duckduckgo.app.browser.viewstate

import com.duckduckgo.app.browser.SSLErrorType
import com.duckduckgo.app.browser.SpecialUrlDetector
import com.duckduckgo.app.browser.WebViewErrorResponse
import com.duckduckgo.app.global.model.MaliciousSiteStatus
import com.duckduckgo.privacyprotectionspopup.api.PrivacyProtectionsPopupViewState
import com.duckduckgo.savedsites.api.models.SavedSite

data class BrowserViewState(
    val browserShowing: Boolean = false,
    val isFullScreen: Boolean = false,
    val isDesktopBrowsingMode: Boolean = false,
    val canChangeBrowsingMode: Boolean = false,
    val showPrivacyShield: HighlightableButton = HighlightableButton.Visible(enabled = false),
    val fireButton: HighlightableButton = HighlightableButton.Visible(),
    val showMenuButton: HighlightableButton = HighlightableButton.Visible(),
    val showSelectDefaultBrowserMenuItem: Boolean = false,
    val canSharePage: Boolean = false,
    val canSaveSite: Boolean = false,
    val bookmark: SavedSite.Bookmark? = null,
    val favorite: SavedSite.Favorite? = null,
    val canFireproofSite: Boolean = false,
    val isFireproofWebsite: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val canChangePrivacyProtection: Boolean = false,
    val isPrivacyProtectionDisabled: Boolean = false,
    val canReportSite: Boolean = false,
    val addToHomeEnabled: Boolean = false,
    val addToHomeVisible: Boolean = false,
    val isEmailSignedIn: Boolean = false,
    var previousAppLink: SpecialUrlDetector.UrlType.AppLink? = null,
    val canFindInPage: Boolean = false,
    val forceRenderingTicker: Long = System.currentTimeMillis(),
    val canPrintPage: Boolean = false,
    val isPrinting: Boolean = false,
    val showAutofill: Boolean = false,
    val browserError: WebViewErrorResponse = WebViewErrorResponse.OMITTED,
    val sslError: SSLErrorType = SSLErrorType.NONE,
    val maliciousSiteBlocked: Boolean = false,
    val maliciousSiteStatus: MaliciousSiteStatus? = null,
    val privacyProtectionsPopupViewState: PrivacyProtectionsPopupViewState = PrivacyProtectionsPopupViewState.Gone,
    val showDuckChatOption: Boolean = false,
)

sealed class HighlightableButton {
    data class Visible(
        val enabled: Boolean = true,
        val highlighted: Boolean = false,
    ) : HighlightableButton()

    object Gone : HighlightableButton()

    fun isHighlighted(): Boolean {
        return when (this) {
            is Visible -> this.highlighted
            is Gone -> false
        }
    }

    fun isEnabled(): Boolean {
        return when (this) {
            is Visible -> this.enabled
            is Gone -> false
        }
    }
}
