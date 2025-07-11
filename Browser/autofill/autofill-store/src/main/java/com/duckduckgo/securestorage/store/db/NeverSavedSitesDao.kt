

package com.duckduckgo.securestorage.store.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NeverSavedSitesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(neverSavedSiteEntity: NeverSavedSiteEntity): Long

    @Query("delete from never_saved_sites")
    fun clear()

    @Query("select count(*) from never_saved_sites")
    fun count(): Flow<Int>

    @Query("select count(*) from never_saved_sites where domain = :url")
    fun isInNeverSaveList(url: String): Boolean
}
