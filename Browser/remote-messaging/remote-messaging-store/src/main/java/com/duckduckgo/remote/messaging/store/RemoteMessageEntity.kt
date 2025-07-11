

package com.duckduckgo.remote.messaging.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_message")
data class RemoteMessageEntity(
    @PrimaryKey val id: String,
    val message: String,
    val status: Status,
    val shown: Boolean = false,
) {
    enum class Status {
        SCHEDULED,
        DISMISSED,
        DONE,
    }
}
