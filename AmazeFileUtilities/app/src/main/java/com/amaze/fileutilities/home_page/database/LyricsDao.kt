package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LyricsDao {

    @Query("SELECT * FROM lyrics WHERE file_path=:path")
    fun findByPath(path: String): Lyrics?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lyrics: Lyrics)

    @Delete
    fun delete(lyrics: Lyrics)
}
