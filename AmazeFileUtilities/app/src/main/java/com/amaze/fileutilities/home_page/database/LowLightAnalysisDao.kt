package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LowLightAnalysisDao {

    @Query("SELECT * FROM lowlightanalysis WHERE file_path=:path")
    fun findByPath(path: String): LowLightAnalysis?

    @Query("SELECT * FROM lowlightanalysis where is_low_light=1")
    fun getAllLowLight(): List<LowLightAnalysis>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(analysis: LowLightAnalysis)

    @Delete
    fun delete(user: LowLightAnalysis)

    @Query("DELETE FROM lowlightanalysis WHERE file_path like '%' || :path || '%'")
    fun deleteByPathContains(path: String)

    @Query("UPDATE lowlightanalysis SET is_low_light=0 WHERE file_path IN(:pathList)")
    fun cleanIsLowLight(pathList: List<String>)
}
