

package com.duckduckgo.app.browser.pageloadpixel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_loaded_pixel_entity")
class PageLoadedPixelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appVersion: String,
    val elapsedTime: Long,
    val webviewVersion: String,
    val trackerOptimizationEnabled: Boolean,
    val cpmEnabled: Boolean,
)
