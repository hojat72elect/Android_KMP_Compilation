

package com.duckduckgo.app.statistics.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duckduckgo.app.statistics.model.UniquePixelFired

@Dao
interface UniquePixelFiredDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: UniquePixelFired)

    @Query("SELECT COUNT(1) > 0 FROM unique_pixels_fired WHERE name = :name")
    suspend fun hasUniquePixelFired(name: String): Boolean
}
