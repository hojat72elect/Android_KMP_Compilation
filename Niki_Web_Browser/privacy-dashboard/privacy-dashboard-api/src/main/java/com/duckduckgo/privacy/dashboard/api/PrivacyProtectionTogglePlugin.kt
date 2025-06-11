

package com.duckduckgo.privacy.dashboard.api

interface PrivacyProtectionTogglePlugin {
    /**
     * Executed when the privacy toggle is switched on. It receives the [PrivacyToggleOrigin].
     */
    suspend fun onToggleOn(origin: PrivacyToggleOrigin)

    /**
     * Executed when the privacy toggle is switched off. It receives the [PrivacyToggleOrigin].
     */
    suspend fun onToggleOff(origin: PrivacyToggleOrigin)
}

enum class PrivacyToggleOrigin {
    MENU,
    DASHBOARD,
    BREAKAGE_FORM,
}
