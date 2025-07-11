

package com.duckduckgo.common.utils.formatters.time.model

import android.content.res.Resources
import com.duckduckgo.common.utils.R
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import java.time.LocalDateTime

fun dateOfPreviousMidnight(): String {
    val midnight = LocalDateTime.now().toLocalDate().atStartOfDay()
    return DatabaseDateFormatter.timestamp(midnight)
}

fun dateOfLastDay(): String {
    val day = LocalDateTime.now().minusHours(24)
    return DatabaseDateFormatter.timestamp(day)
}

fun dateOfLastHour(): String {
    val midnight = LocalDateTime.now().minusHours(1)
    return DatabaseDateFormatter.timestamp(midnight)
}

fun dateOfLastWeek(): String {
    val midnight = LocalDateTime.now().minusDays(7)
    return DatabaseDateFormatter.timestamp(midnight)
}

data class TimePassed(
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
) {

    fun shortFormat(resources: Resources): String {
        if (hours > 0) {
            return resources.getString(R.string.common_HoursAgo, hours)
        }

        if (minutes > 2) {
            return resources.getString(R.string.common_MinutesAgo, minutes)
        }

        return resources.getString(R.string.common_JustNow)
    }

    fun format(
        alwaysShowHours: Boolean = true,
        alwaysShowMinutes: Boolean = true,
        alwaysShowSeconds: Boolean = true,
        resources: Resources,
    ): String {
        val sb = StringBuilder()

        if (hours > 0 || alwaysShowHours) {
            sb.append(hours)
            sb.append(" ${resources.getString(R.string.common_hour_abbreviation)}")
        }

        if (minutes > 0 || alwaysShowMinutes) {
            sb.append(" ")
            sb.append(minutes)
            sb.append(" ${resources.getString(R.string.common_min_abbreviation)}")
        }

        if (alwaysShowSeconds) {
            sb.append(" ")
            sb.append(seconds)
            sb.append(" ${resources.getString(R.string.common_seconds_abbreviation)}")
        }

        return sb.toString()
    }

    companion object {

        fun between(
            currentMillis: Long,
            oldMillis: Long,
        ): TimePassed {
            return fromMilliseconds(currentMillis - oldMillis)
        }

        fun fromMilliseconds(millis: Long): TimePassed {
            val seconds = (millis / 1000) % 60
            val minutes = (millis / (1000 * 60) % 60)
            val hours = (millis / (1000) / 3600)

            return TimePassed(hours, minutes, seconds)
        }
    }
}
