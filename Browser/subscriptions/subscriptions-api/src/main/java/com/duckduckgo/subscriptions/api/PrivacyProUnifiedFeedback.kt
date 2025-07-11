

package com.duckduckgo.subscriptions.api

interface PrivacyProUnifiedFeedback {
    /**
     * @return `true` if for the given source, we should allow the usage of unified feedback flow or `false` otherwise
     */
    suspend fun shouldUseUnifiedFeedback(source: PrivacyProFeedbackSource): Boolean

    enum class PrivacyProFeedbackSource {
        DDG_SETTINGS,
        SUBSCRIPTION_SETTINGS,
        VPN_MANAGEMENT,
        VPN_EXCLUDED_APPS,
        UNKNOWN,
    }
}
