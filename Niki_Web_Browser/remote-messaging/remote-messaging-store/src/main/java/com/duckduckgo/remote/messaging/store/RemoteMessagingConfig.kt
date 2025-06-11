

package com.duckduckgo.remote.messaging.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "remote_messaging")
data class RemoteMessagingConfig(
    @PrimaryKey
    val id: Int = 1,
    val version: Long,
    val invalidate: Boolean = false,
    val evaluationTimestamp: String = databaseTimestampFormatter().format(LocalDateTime.now()),
)

fun RemoteMessagingConfig.expired(): Boolean {
    val yesterday = databaseTimestampFormatter().format(LocalDateTime.now().minusDays(1L))
    return this.evaluationTimestamp < yesterday
}

fun RemoteMessagingConfig.invalidated(): Boolean {
    return this.invalidate
}
