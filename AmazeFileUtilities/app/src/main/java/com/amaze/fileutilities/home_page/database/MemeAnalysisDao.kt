package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemeAnalysisDao {

    @Query("SELECT * FROM memeanalysis WHERE file_path=:path")
    fun findByPath(path: String): MemeAnalysis?

    @Query("SELECT * FROM memeanalysis where is_meme=1")
    fun getAllMeme(): List<MemeAnalysis>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(analysis: MemeAnalysis)

    @Delete
    fun delete(user: MemeAnalysis)

    @Query("DELETE FROM memeanalysis WHERE file_path like '%' || :path || '%'")
    fun deleteByPathContains(path: String)

    @Query("UPDATE memeanalysis SET is_meme=0 WHERE file_path IN(:pathList)")
    fun cleanIsMeme(pathList: List<String>)
}
