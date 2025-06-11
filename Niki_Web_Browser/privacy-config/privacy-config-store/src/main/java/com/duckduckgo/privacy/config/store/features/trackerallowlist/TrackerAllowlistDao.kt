

package com.duckduckgo.privacy.config.store.features.trackerallowlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.TrackerAllowlistEntity

@Dao
abstract class TrackerAllowlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<TrackerAllowlistEntity>)

    @Transaction
    open fun updateAll(trackers: List<TrackerAllowlistEntity>) {
        deleteAll()
        insertAll(trackers)
    }

    @Query("select * from tracker_allowlist where domain = :domain")
    abstract fun get(domain: String): TrackerAllowlistEntity

    @Query("select * from tracker_allowlist")
    abstract fun getAll(): List<TrackerAllowlistEntity>

    @Query("delete from tracker_allowlist")
    abstract fun deleteAll()
}
