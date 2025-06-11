

package com.duckduckgo.networkprotection.impl

data class NetworkProtectionStatistics(
    val publicKey: String = "",
    val serverIP: String = "",
    val receivedBytes: Long = 0L,
    val transmittedBytes: Long = 0L,
    val lastHandshakeEpochSeconds: Long = 0L,
)
