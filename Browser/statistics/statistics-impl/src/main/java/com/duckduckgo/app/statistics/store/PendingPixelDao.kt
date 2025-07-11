

package com.duckduckgo.app.statistics.store

import androidx.room.*
import com.duckduckgo.app.statistics.model.PixelEntity
import io.reactivex.Observable

@Dao
interface PendingPixelDao {

    @Query("select * from pixel_store")
    fun pixels(): Observable<List<PixelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pixel: PixelEntity): Long

    @Delete fun delete(pixel: PixelEntity)
}
