

package com.duckduckgo.app.trackerdetection.db

import androidx.room.*
import com.duckduckgo.app.trackerdetection.model.TdsTracker

@Dao
abstract class TdsTrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(trackers: Collection<TdsTracker>)

    @Transaction
    open fun updateAll(trackers: Collection<TdsTracker>) {
        deleteAll()
        insertAll(trackers)
    }

    @Query("select * from tds_tracker")
    abstract fun getAll(): List<TdsTracker>

    @Query("select * from tds_tracker where domain = :domain")
    abstract fun get(domain: String): TdsTracker?

    @Query("select count(*) from tds_tracker")
    abstract fun count(): Int

    @Query("delete from tds_tracker")
    abstract fun deleteAll()
}
