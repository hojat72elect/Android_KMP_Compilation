package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BlurAnalysisDao {

    @Query("SELECT * FROM bluranalysis WHERE file_path=:path")
    fun findByPath(path: String): BlurAnalysis?

    @Query("SELECT * FROM bluranalysis where is_blur=1")
    fun getAllBlur(): List<BlurAnalysis>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(analysis: BlurAnalysis)

    @Delete
    fun delete(user: BlurAnalysis)

    @Query("DELETE FROM bluranalysis WHERE file_path like '%' || :path || '%'")
    fun deleteByPathContains(path: String)

    @Query("UPDATE bluranalysis SET is_blur=0 WHERE file_path IN(:pathList)")
    fun cleanIsBlur(pathList: List<String>)
}
