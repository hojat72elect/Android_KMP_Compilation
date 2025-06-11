
package com.duckduckgo.elementhiding.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "element_hiding")
data class ElementHidingEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)
