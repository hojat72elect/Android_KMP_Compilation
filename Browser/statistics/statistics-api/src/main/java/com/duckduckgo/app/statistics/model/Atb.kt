

package com.duckduckgo.app.statistics.model

/**
 * Represents ATB returned by calling /atb.js
 */
data class Atb(
    val version: String,
    val updateVersion: String? = null,
) {

    fun formatWithVariant(variantKey: String?): String {
        return version + variantKey.orEmpty()
    }
}
