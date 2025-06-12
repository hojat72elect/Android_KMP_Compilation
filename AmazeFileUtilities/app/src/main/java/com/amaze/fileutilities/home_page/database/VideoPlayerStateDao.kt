package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoPlayerStateDao {

    @Query("SELECT * FROM videoplayerstate WHERE file_path=:filePath")
    fun getStateByUriPath(filePath: String): VideoPlayerState?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playerState: VideoPlayerState)
}
