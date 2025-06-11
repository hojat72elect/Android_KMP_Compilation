

package com.duckduckgo.app.statistics.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unique_pixels_fired")
data class UniquePixelFired(
    @PrimaryKey val name: String,
)
