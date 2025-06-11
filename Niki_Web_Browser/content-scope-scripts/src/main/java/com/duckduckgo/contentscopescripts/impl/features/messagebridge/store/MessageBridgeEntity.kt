package com.duckduckgo.contentscopescripts.impl.features.messagebridge.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_bridge")
data class MessageBridgeEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)
