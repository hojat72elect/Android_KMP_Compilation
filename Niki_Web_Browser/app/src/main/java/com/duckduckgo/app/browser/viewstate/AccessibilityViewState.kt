

package com.duckduckgo.app.browser.viewstate

import com.duckduckgo.app.accessibility.data.AccessibilitySettingsSharedPreferences

data class AccessibilityViewState(
    val fontSize: Float = AccessibilitySettingsSharedPreferences.FONT_SIZE_DEFAULT,
    val forceZoom: Boolean = false,
    val refreshWebView: Boolean = false,
)
