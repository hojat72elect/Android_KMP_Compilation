

package com.duckduckgo.networkprotection.impl.settings.geoswitching

import java.util.*

private const val REGIONAL_INDICATOR_OFFSET = 0x1F1A5

/*
 * The regional indicators go from 0x1F1E6 (A) to 0x1F1FF (Z).
 * This is the A regional indicator value minus 65 decimal so
 * that we can just add this to the A-Z char
 */
internal fun getEmojiForCountryCode(countryCode: String): String {
    @Suppress("NAME_SHADOWING")
    val countryCode = countryCode.uppercase()

    if (countryCode.isBlank()) return "🏳️"
    if (countryCode.length > 2) return "🏳️"
    val (first, second) = countryCode.toCharArray().run {
        this[0] to this[1]
    }
    if (first < 'A' || first > 'Z') return "🏳️"
    if (second < 'A' || second > 'Z') return "🏳️"

    return countryCode.uppercase()
        .split("")
        .filter { it.isNotBlank() }
        .map { it.codePointAt(0) + REGIONAL_INDICATOR_OFFSET }
        .joinToString("") { String(Character.toChars(it)) }
}

internal fun getDisplayableCountry(countryCode: String): String {
    return Locale("", countryCode).displayCountry
}
