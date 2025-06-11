

package com.duckduckgo.app.browser.pageloadpixel.firstpaint

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class PagePaintedPixelDao {

    @Insert
    abstract fun add(entity: PagePaintedPixelEntity)

    @Query("SELECT * FROM page_painted_pixel_entity")
    abstract fun all(): List<PagePaintedPixelEntity>

    @Delete
    abstract fun delete(entity: PagePaintedPixelEntity)

    @Query("DELETE FROM page_painted_pixel_entity")
    abstract fun deleteAll()
}
