

package com.duckduckgo.app.statistics.user_segments

internal fun String.parseAtbWeek(): Int {
    val startIndex = this.indexOf('v') + 1
    val endIndex = this.indexOf('-', startIndex)
    return if (endIndex > 0 && startIndex < endIndex) {
        this.substring(startIndex, endIndex).toInt()
    } else {
        this.substringAfter('v', "").toInt()
    }
}

internal fun String.asNumber(): Int {
    fun String.parseDay(): Int? {
        val day = this.substringAfterLast('-', "").firstOrNull()
        return day?.digitToIntOrNull()
    }
    val week = this.parseAtbWeek()
    val day = this.parseDay() ?: 0

    return week * 7 + day - 1
}
