
package com.duckduckgo.webcompat.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "web_compat")
data class WebCompatEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)
