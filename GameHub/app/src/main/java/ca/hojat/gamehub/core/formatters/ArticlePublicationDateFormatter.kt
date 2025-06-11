package ca.hojat.gamehub.core.formatters

import ca.hojat.gamehub.core.providers.TimeFormat
import ca.hojat.gamehub.core.providers.TimeFormatProvider
import ca.hojat.gamehub.core.providers.TimeProvider
import com.paulrybitskyi.hiltbinder.BindType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

interface ArticlePublicationDateFormatter {
    fun formatPublicationDate(timestamp: Long): String
}

@BindType
class ArticlePublicationDateFormatterImpl @Inject constructor(
    private val relativeDateFormatter: RelativeDateFormatter,
    private val timeProvider: TimeProvider,
    private val timeFormatProvider: TimeFormatProvider
) : ArticlePublicationDateFormatter {

    private companion object {
        private const val ABS_DATE_24_HOUR_PATTERN_WITHOUT_YEAR = "MMM d, H:mm"
        private const val ABS_DATE_12_HOUR_PATTERN_WITHOUT_YEAR = "MMM d, h:mm a"

        private const val ABS_DATE_24_HOUR_PATTERN_WITH_YEAR = "MMM d, yyyy, H:mm"
        private const val ABS_DATE_12_HOUR_PATTERN_WITH_YEAR = "MMM d, yyyy, h:mm a"
    }

    override fun formatPublicationDate(timestamp: Long): String {
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()
        )

        return if (shouldFormatAsRelativeDate(dateTime)) {
            relativeDateFormatter.formatRelativeDate(dateTime)
        } else {
            formatAsAbsoluteDate(dateTime)
        }
    }

    private fun shouldFormatAsRelativeDate(dateTime: LocalDateTime): Boolean {
        val currentDateTime = timeProvider.getCurrentDateTime()
        val dayDiffCount = ChronoUnit.DAYS.between(dateTime, currentDateTime)

        return (dayDiffCount == 0L)
    }

    private fun formatAsAbsoluteDate(dateTime: LocalDateTime): String {
        val pattern = getAbsoluteDatePattern(dateTime)

        return DateTimeFormatter.ofPattern(pattern).format(dateTime)
    }

    private fun getAbsoluteDatePattern(dateTime: LocalDateTime): String {
        val currentDateTime = timeProvider.getCurrentDateTime()
        val yearDiffCount = ChronoUnit.YEARS.between(dateTime, currentDateTime).toInt()
        val hasYearDiff = (yearDiffCount > 0)

        return when (timeFormatProvider.getTimeFormat()) {

            TimeFormat.TWENTY_FOUR_HOURS -> if (hasYearDiff) {
                ABS_DATE_24_HOUR_PATTERN_WITH_YEAR
            } else {
                ABS_DATE_24_HOUR_PATTERN_WITHOUT_YEAR
            }

            TimeFormat.TWELVE_HOURS -> if (hasYearDiff) {
                ABS_DATE_12_HOUR_PATTERN_WITH_YEAR
            } else {
                ABS_DATE_12_HOUR_PATTERN_WITHOUT_YEAR
            }
        }
    }
}
