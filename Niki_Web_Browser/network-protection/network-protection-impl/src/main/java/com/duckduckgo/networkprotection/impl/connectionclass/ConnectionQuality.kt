

package com.duckduckgo.networkprotection.impl.connectionclass

/**
 * excellent -> [0, 20]
 * good -> (20, 50]
 * moderate -> (50, 200]
 * poor -> (200, 300]
 * terrible -> (300, any)
 */
enum class ConnectionQuality(val max: Double, val emoji: String) {
    TERRIBLE(Double.MAX_VALUE, "🥺"),
    POOR(300.0, "☹️"),
    MODERATE(200.0, "😐"),
    GOOD(50.0, "🙂"),
    EXCELLENT(20.0, "🤗"),
    UNKNOWN(Double.MAX_VALUE, "🤔"),
}

fun Double.asConnectionQuality(): ConnectionQuality {
    return if (this <= 0) {
        ConnectionQuality.UNKNOWN
    } else if (this <= ConnectionQuality.EXCELLENT.max) {
        ConnectionQuality.EXCELLENT
    } else if (this <= ConnectionQuality.GOOD.max) {
        ConnectionQuality.GOOD
    } else if (this <= ConnectionQuality.MODERATE.max) {
        ConnectionQuality.MODERATE
    } else if (this <= ConnectionQuality.POOR.max) {
        ConnectionQuality.POOR
    } else if (this <= ConnectionQuality.TERRIBLE.max) {
        ConnectionQuality.TERRIBLE
    } else {
        ConnectionQuality.UNKNOWN
    }
}

fun Int.asConnectionQuality(): ConnectionQuality {
    return this.toDouble().asConnectionQuality()
}
