

package com.duckduckgo.common.utils.formatters.time

import android.content.Context
import com.duckduckgo.common.utils.R
import com.duckduckgo.common.utils.formatters.time.model.TimePassed
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface TimeDiffFormatter {
    fun formatTimePassedInDaysWeeksMonthsYears(
        endLocalDateTime: LocalDateTime = LocalDateTime.now(),
        startLocalDateTime: LocalDateTime,
    ): String

    fun formatTimePassedInDays(
        endLocalDateTime: LocalDateTime = LocalDateTime.now(),
        startLocalDateTime: LocalDateTime,
    ): String

    fun formatTimePassed(
        endLocalDateTime: LocalDateTime = LocalDateTime.now(),
        startLocalDateTime: LocalDateTime,
    ): String
}

@ContributesBinding(AppScope::class)
class RealTimeDiffFormatter @Inject constructor(private val context: Context) : TimeDiffFormatter {

    override fun formatTimePassedInDaysWeeksMonthsYears(
        endLocalDateTime: LocalDateTime,
        startLocalDateTime: LocalDateTime,
    ): String {
        val startYear = startLocalDateTime.year
        val endYear = endLocalDateTime.year

        val diffYear = endYear - startYear

        if (diffYear > 0) {
            return startYear.toString()
        }

        val startDate = DatabaseDateFormatter.timestamp(startLocalDateTime).substringBefore("T")
            .run { FORMATTER_DAYS.parse(this) }
        val endDate = DatabaseDateFormatter.timestamp(endLocalDateTime).substringBefore("T")
            .run { FORMATTER_DAYS.parse(this) }

        val diff = (endDate.time - startDate.time).run { TimeUnit.DAYS.convert(this, TimeUnit.MILLISECONDS) }

        return when {
            diff == 0L -> context.getString(R.string.common_Today)
            diff == 1L -> context.getString(R.string.common_Yesterday)
            diff <= 7L -> context.getString(R.string.common_PastWeek)
            diff <= 30L -> context.getString(R.string.common_PastMonth)
            else -> SimpleDateFormat(PATTERN_MONTH_NAME, Locale.getDefault()).format(startDate)
        }
    }

    override fun formatTimePassedInDays(
        endLocalDateTime: LocalDateTime,
        startLocalDateTime: LocalDateTime,
    ): String {
        val startDate = DatabaseDateFormatter.timestamp(startLocalDateTime).substringBefore("T")
            .run { SimpleDateFormat(PATTERN_FORMATTER_DAYS).parse(this) }
        val endDate = DatabaseDateFormatter.timestamp(endLocalDateTime).substringBefore("T")
            .run { SimpleDateFormat(PATTERN_FORMATTER_DAYS).parse(this) }

        val diff = (endDate.time - startDate.time).run { TimeUnit.DAYS.convert(this, TimeUnit.MILLISECONDS) }

        return when (diff) {
            0L -> context.getString(R.string.common_Today)
            1L -> context.getString(R.string.common_Yesterday)
            else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(startDate)
        }
    }

    override fun formatTimePassed(
        endLocalDateTime: LocalDateTime,
        startLocalDateTime: LocalDateTime,
    ): String {
        val timeDifferenceMillis = Duration.between(startLocalDateTime, endLocalDateTime).toMillis()
        val startDate = DatabaseDateFormatter.timestamp(startLocalDateTime).substringBefore("T")
            .run { SimpleDateFormat(PATTERN_FORMATTER_DAYS).parse(this) }
        val endDate = DatabaseDateFormatter.timestamp(endLocalDateTime).substringBefore("T")
            .run { SimpleDateFormat(PATTERN_FORMATTER_DAYS).parse(this) }

        val timeDifferenceDate = (endDate.time - startDate.time).run { TimeUnit.DAYS.convert(this, TimeUnit.MILLISECONDS) }

        return when (timeDifferenceDate) {
            0L, 1L -> TimePassed.fromMilliseconds(timeDifferenceMillis).shortFormat(context.resources)
            else -> context.getString(R.string.common_DaysAgo, timeDifferenceDate)
        }
    }

    companion object {
        private val FORMATTER_DAYS = SimpleDateFormat("yyyy-MM-dd")
        private const val PATTERN_FORMATTER_DAYS = "yyyy-MM-dd"
        private const val PATTERN_MONTH_NAME = "MMMM"
    }
}
