

package com.duckduckgo.app.privacy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "privacy_protection_count")
data class PrivacyProtectionCountsEntity(
    @PrimaryKey val key: String = SINGLETON_KEY,

    @ColumnInfo(name = "blocked_tracker_count")
    val blockedTrackerCount: Long,

    @ColumnInfo(name = "upgrade_count")
    val upgradeCount: Long,

) {
    companion object {
        const val SINGLETON_KEY = "SINGLETON_KEY"
    }
}
