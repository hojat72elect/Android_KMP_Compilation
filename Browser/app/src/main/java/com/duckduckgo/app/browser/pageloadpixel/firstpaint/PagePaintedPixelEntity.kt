

package com.duckduckgo.app.browser.pageloadpixel.firstpaint

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_painted_pixel_entity")
class PagePaintedPixelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appVersion: String,
    val elapsedTimeFirstPaint: Long,
    val webViewVersion: String,
)
