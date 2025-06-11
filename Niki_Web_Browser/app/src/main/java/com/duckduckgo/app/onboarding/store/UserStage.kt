

package com.duckduckgo.app.onboarding.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

const val USER_STAGE_TABLE_NAME = "userStage"

@Entity(tableName = USER_STAGE_TABLE_NAME)
data class UserStage(
    @PrimaryKey val key: Int = 1,
    val appStage: AppStage,
)

enum class AppStage {
    NEW,
    DAX_ONBOARDING,
    ESTABLISHED,
    ;
}

class StageTypeConverter {

    @TypeConverter
    fun toStage(stage: String): AppStage {
        return try {
            AppStage.valueOf(stage)
        } catch (ex: IllegalArgumentException) {
            AppStage.ESTABLISHED
        }
    }

    @TypeConverter
    fun fromStage(stage: AppStage): String {
        return stage.name
    }
}
