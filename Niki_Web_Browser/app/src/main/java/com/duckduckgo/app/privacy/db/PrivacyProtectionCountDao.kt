

package com.duckduckgo.app.privacy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.app.privacy.model.PrivacyProtectionCountsEntity

@Dao
abstract class PrivacyProtectionCountDao {

    @Query("SELECT blocked_tracker_count FROM privacy_protection_count LIMIT 1")
    abstract fun getTrackersBlockedCount(): Long

    @Query("SELECT upgrade_count FROM privacy_protection_count LIMIT 1")
    abstract fun getUpgradeCount(): Long

    @Transaction
    open fun incrementUpgradeCount() {
        val changedRows = incrementUpgradeCountIfExists()
        if (changedRows == 0) {
            initialiseCounts(PrivacyProtectionCountsEntity(blockedTrackerCount = 0, upgradeCount = 1))
        }
    }

    @Transaction
    open fun incrementBlockedTrackerCount() {
        val changedRows = incrementBlockedTrackerCountIfExists()
        if (changedRows == 0) {
            initialiseCounts(PrivacyProtectionCountsEntity(blockedTrackerCount = 1, upgradeCount = 0))
        }
    }

    @Query("UPDATE privacy_protection_count SET blocked_tracker_count = blocked_tracker_count + 1")
    protected abstract fun incrementBlockedTrackerCountIfExists(): Int

    @Query("UPDATE privacy_protection_count SET upgrade_count = upgrade_count + 1")
    protected abstract fun incrementUpgradeCountIfExists(): Int

    @Insert
    abstract fun initialiseCounts(entity: PrivacyProtectionCountsEntity)
}
