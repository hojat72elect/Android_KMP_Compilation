

package com.duckduckgo.app.anrs.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnrDao {
    @Query("SELECT * FROM anr_entity")
    fun getAnrs(): List<AnrEntity>

    @Query("SELECT * FROM anr_entity")
    fun getAnrsFlow(): Flow<List<AnrEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(anr: AnrEntity)

    @Query("SELECT EXISTS(SELECT * FROM anr_entity WHERE hash = :hash)")
    fun anrExists(hash: String): Boolean

    @Query("DELETE FROM anr_entity WHERE hash = :hash")
    fun deleteAnr(hash: String)

    @Query("SELECT * FROM anr_entity ORDER BY timestamp DESC LIMIT 1")
    fun latestAnr(): AnrEntity?
}
