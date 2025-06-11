

package com.duckduckgo.mobile.android.vpn.dao

import androidx.room.*
import com.duckduckgo.mobile.android.vpn.model.VpnTracker
import com.duckduckgo.mobile.android.vpn.model.VpnTrackerCompanySignal
import kotlinx.coroutines.flow.Flow

@Dao
internal interface VpnTrackerDao {

    @Transaction
    fun insert(tracker: VpnTracker) {
        getTrackerFor(tracker.bucket, tracker.domain, tracker.trackingApp.packageId)?.let {
            incrementCount(tracker.bucket, tracker.timestamp, tracker.domain, tracker.trackingApp.packageId)
        } ?: internalInsertTracker(tracker.copy(count = 1))
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun internalInsertTracker(tracker: VpnTracker)

    @Transaction
    fun insert(tracker: List<VpnTracker>) {
        tracker.forEach { insert(it) }
    }

    @Query("UPDATE vpn_tracker SET count = count + 1, timestamp = :timestamp WHERE bucket = :bucket AND domain = :domain AND packageId = :packageId")
    fun incrementCount(bucket: String, timestamp: String, domain: String, packageId: String)

    @Query("SELECT * FROM vpn_tracker WHERE bucket = :bucket AND domain = :domain AND packageId = :packageId LIMIT 1")
    fun getTrackerFor(bucket: String, domain: String, packageId: String): VpnTracker?

    @Query("DELETE FROM vpn_tracker")
    fun deleteAllTrackers()

    @Query("SELECT * FROM vpn_tracker ORDER BY timestamp DESC LIMIT 1")
    fun getLatestTracker(): Flow<VpnTrackerCompanySignal?>

    @Query(
        "SELECT * FROM vpn_tracker " +
            "WHERE timestamp >= :startTime AND timestamp < :endTime ORDER BY timestamp DESC limit $MAX_NUMBER_OF_TRACKERS_IN_QUERY_RESULTS",
    )
    fun getTrackersBetween(
        startTime: String,
        endTime: String,
    ): Flow<List<VpnTrackerCompanySignal>>

    @Query(
        "SELECT * FROM vpn_tracker " +
            "WHERE timestamp >= :startTime AND timestamp < :endTime ORDER BY timestamp DESC limit $MAX_NUMBER_OF_TRACKERS_IN_QUERY_RESULTS",
    )
    fun getTrackersBetweenSync(
        startTime: String,
        endTime: String,
    ): List<VpnTrackerCompanySignal>

    @Query("DELETE FROM vpn_tracker WHERE timestamp < :startTime")
    fun deleteOldDataUntil(startTime: String)

    @Query(
        """
            SELECT COALESCE(sum(count), 0) 
            FROM vpn_tracker 
            JOIN vpn_app_tracker_entities ON vpn_tracker.trackerCompanyId = vpn_app_tracker_entities.trackerCompanyId 
            WHERE timestamp >= :startTime AND timestamp < :endTime
        """,
    )
    fun getTrackersCountBetween(
        startTime: String,
        endTime: String,
    ): Flow<Int>

    @Query(
        """
            SELECT COUNT(DISTINCT packageId) 
            FROM vpn_tracker 
            JOIN vpn_app_tracker_entities ON vpn_tracker.trackerCompanyId = vpn_app_tracker_entities.trackerCompanyId 
            WHERE timestamp >= :startTime AND timestamp < :endTime
        """,
    )
    fun getTrackingAppsCountBetween(
        startTime: String,
        endTime: String,
    ): Flow<Int>

    @Query(
        "SELECT * FROM vpn_tracker " +
            "WHERE timestamp >= :startTime order by timestamp DESC limit $MAX_NUMBER_OF_TRACKERS_IN_QUERY_RESULTS",
    )
    fun getPagedTrackersSince(startTime: String): Flow<List<VpnTrackerCompanySignal>>

    @Transaction
    @Query(
        "SELECT * FROM vpn_tracker " +
            "WHERE timestamp LIKE :date || '%' AND packageId = :appPackage" +
            " order by timestamp" +
            " DESC limit $MAX_NUMBER_OF_TRACKERS_IN_QUERY_RESULTS",
    )
    fun getTrackersForAppFromDate(
        date: String,
        appPackage: String,
    ): Flow<List<VpnTrackerCompanySignal>>

    @Query("SELECT * from vpn_tracker WHERE packageId = :appPackage")
    fun getTrackersForApp(appPackage: String): List<VpnTrackerCompanySignal>

    @Query("select count(1) > 0 from vpn_tracker LIMIT 1")
    fun tableIsNotEmpty(): Boolean

    companion object {
        private const val MAX_NUMBER_OF_TRACKERS_IN_QUERY_RESULTS = 10_000
    }
}
