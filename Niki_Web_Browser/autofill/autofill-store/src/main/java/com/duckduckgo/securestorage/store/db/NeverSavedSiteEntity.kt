

package com.duckduckgo.securestorage.store.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "never_saved_sites",
    indices = [Index(value = ["domain"], unique = true)],
)
data class NeverSavedSiteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val domain: String?,
)
