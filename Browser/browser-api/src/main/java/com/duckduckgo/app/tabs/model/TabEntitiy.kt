

package com.duckduckgo.app.tabs.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import java.time.LocalDateTime

@Entity(
    tableName = "tabs",
    foreignKeys = [
        ForeignKey(
            entity = TabEntity::class,
            parentColumns = ["tabId"],
            childColumns = ["sourceTabId"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index("tabId"),
    ],
)
data class TabEntity(
    @PrimaryKey val tabId: String,
    val url: String? = null,
    val title: String? = null,
    val skipHome: Boolean = false,
    val viewed: Boolean = true,
    val position: Int = 0,
    val tabPreviewFile: String? = null,
    val sourceTabId: String? = null,
    val deletable: Boolean = false,
    val lastAccessTime: LocalDateTime? = null,
)

val TabEntity.isBlank: Boolean
    get() = title == null && url == null

class LocalDateTimeTypeConverter {
    @TypeConverter
    fun convertForDb(date: LocalDateTime): String = DatabaseDateFormatter.timestamp(date)

    @TypeConverter
    fun convertFromDb(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }
}
