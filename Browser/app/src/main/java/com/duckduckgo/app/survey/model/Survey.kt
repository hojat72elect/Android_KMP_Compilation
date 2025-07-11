

package com.duckduckgo.app.survey.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable

@Entity(
    tableName = "survey",
)
data class Survey(
    @PrimaryKey val surveyId: String,
    val url: String?,
    val daysInstalled: Int?,
    var status: Status,
) : Serializable {

    enum class Status {
        NOT_ALLOCATED,
        SCHEDULED,
        CANCELLED,
        DONE,
    }

    class StatusTypeConverter {

        @TypeConverter
        fun toStatus(value: String): Status {
            return Status.valueOf(value)
        }

        @TypeConverter
        fun fromStatus(value: Status): String {
            return value.name
        }
    }
}
