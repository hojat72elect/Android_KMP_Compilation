package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InternalStorageAnalysisDao {

    @Query("SELECT * FROM internalstorageanalysis where is_mediastore=0")
    fun getAll(): List<InternalStorageAnalysis>

    @Query("SELECT * FROM internalstorageanalysis where depth<=:depth and is_mediastore=0")
    fun getAllShallow(depth: Int): List<InternalStorageAnalysis>

    @Query("SELECT * FROM internalstorageanalysis where is_mediastore=1")
    fun getAllMediaFiles(): List<InternalStorageAnalysis>

    @Query("SELECT * FROM internalstorageanalysis where is_empty=1")
    fun getAllEmptyFiles(): List<InternalStorageAnalysis>

    @Query("SELECT * FROM internalstorageanalysis WHERE sha256_checksum=:sha256Checksum")
    fun findBySha256Checksum(sha256Checksum: String): InternalStorageAnalysis?

    @Query(
        "SELECT * FROM internalstorageanalysis " +
                "WHERE is_mediastore = 1 and sha256_checksum=:sha256Checksum"
    )
    fun findMediaFileBySha256Checksum(sha256Checksum: String): InternalStorageAnalysis?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(analysis: InternalStorageAnalysis)

    @Delete
    fun delete(user: InternalStorageAnalysis)
}
