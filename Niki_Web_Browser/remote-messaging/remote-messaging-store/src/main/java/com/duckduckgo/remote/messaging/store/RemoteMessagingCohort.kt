

package com.duckduckgo.remote.messaging.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_messaging_cohort")
data class RemoteMessagingCohort(
    @PrimaryKey val messageId: String,
    val percentile: Float,
)
