package com.duckduckgo.adclick.impl

data class Exemption(
    val hostTldPlusOne: String,
    val navigationExemptionDeadline: Long,
    val exemptionDeadline: Long,
    val adClickActivePixelFired: Boolean = false,
) {
    fun isExpired(): Boolean {
        val now = System.currentTimeMillis()
        if (exemptionDeadline < now) {
            return true
        }
        return navigationExemptionDeadline != NO_EXPIRY && navigationExemptionDeadline < now
    }

    companion object {
        const val NO_EXPIRY = -1L
    }
}
