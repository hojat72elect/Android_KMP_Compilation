

package com.duckduckgo.app.trackerdetection.db

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class DatabaseDateFormatter {

    companion object {
        private val FORMATTER_SECONDS: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        fun timestamp(date: LocalDateTime = LocalDateTime.now()): String {
            return FORMATTER_SECONDS.format(date)
        }
    }
}
