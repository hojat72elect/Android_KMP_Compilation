

package com.duckduckgo.app.notification.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "notification",
)
data class Notification(
    @PrimaryKey
    val notificationId: String,
)
