
package com.duckduckgo.runtimechecks.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runtime_checks")
data class RuntimeChecksEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)
