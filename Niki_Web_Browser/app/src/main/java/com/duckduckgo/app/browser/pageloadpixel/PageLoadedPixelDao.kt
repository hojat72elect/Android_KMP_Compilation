

package com.duckduckgo.app.browser.pageloadpixel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class PageLoadedPixelDao {

    @Insert
    abstract fun add(pageLoadedPixelEntity: PageLoadedPixelEntity)

    @Query("SELECT * FROM page_loaded_pixel_entity")
    abstract fun all(): List<PageLoadedPixelEntity>

    @Delete
    abstract fun delete(pageLoadedPixelEntity: PageLoadedPixelEntity)

    @Query("DELETE FROM page_loaded_pixel_entity")
    abstract fun deleteAll()
}
