

package com.duckduckgo.app.browser.viewstate

import com.duckduckgo.app.global.model.PrivacyShield

data class PrivacyShieldViewState(
    val privacyShield: PrivacyShield = PrivacyShield.UNKNOWN,
    val trackersBlocked: Int = 0,
)
