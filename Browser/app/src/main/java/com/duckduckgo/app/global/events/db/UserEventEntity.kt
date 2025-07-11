

package com.duckduckgo.app.global.events.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "user_events")
data class UserEventEntity(
    @PrimaryKey val id: UserEventKey,
    val timestamp: Long = System.currentTimeMillis(),
    val payload: String = "",
)

enum class UserEventKey {
    FIRE_BUTTON_HIGHLIGHTED,
    FIRE_BUTTON_EXECUTED,
    FIREPROOF_LOGIN_DIALOG_DISMISSED,
    FIREPROOF_DISABLE_DIALOG_DISMISSED,
    USER_ENABLED_FIREPROOF_LOGIN,
}

class UserEventTypeConverter {

    @TypeConverter
    fun toKey(stage: String): UserEventKey {
        return UserEventKey.valueOf(stage)
    }

    @TypeConverter
    fun fromKey(stage: UserEventKey): String {
        return stage.name
    }
}
