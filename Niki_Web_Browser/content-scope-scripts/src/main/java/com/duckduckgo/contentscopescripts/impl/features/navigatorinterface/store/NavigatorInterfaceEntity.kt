package com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "navigator_interface")
data class NavigatorInterfaceEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)
