

package com.duckduckgo.malicioussiteprotection.api

import android.net.Uri

interface MaliciousSiteProtection {

    suspend fun isMalicious(url: Uri, confirmationCallback: (maliciousStatus: MaliciousStatus) -> Unit): IsMaliciousResult

    fun isFeatureEnabled(): Boolean

    sealed class MaliciousStatus {
        data class Malicious(val feed: Feed) : MaliciousStatus()
        data object Safe : MaliciousStatus()
        data object Ignored : MaliciousStatus()
    }

    enum class Feed {
        PHISHING,
        MALWARE,
        SCAM,
        ;

        companion object {
            fun fromString(name: String): Feed? {
                return try {
                    valueOf(name)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        }
    }

    sealed class IsMaliciousResult {
        data class ConfirmedResult(val status: MaliciousStatus) : IsMaliciousResult()
        data object WaitForConfirmation : IsMaliciousResult()
    }
}
