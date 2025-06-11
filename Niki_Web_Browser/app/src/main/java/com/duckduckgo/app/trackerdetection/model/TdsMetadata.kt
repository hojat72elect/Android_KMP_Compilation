

package com.duckduckgo.app.trackerdetection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tdsMetadata")
data class TdsMetadata(
    @PrimaryKey val id: Int = 1,
    val eTag: String,
)
