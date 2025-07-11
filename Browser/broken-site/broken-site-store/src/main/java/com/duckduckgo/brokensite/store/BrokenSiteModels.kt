

package com.duckduckgo.brokensite.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter

@Entity(tableName = "broken_site_last_sent_report")
data class BrokenSiteLastSentReportEntity(
    @PrimaryKey val hostnameHashPrefix: String,
    val lastSentTimestamp: String = DatabaseDateFormatter.iso8601(),
)
