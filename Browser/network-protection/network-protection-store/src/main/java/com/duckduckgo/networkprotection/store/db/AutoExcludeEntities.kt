

package com.duckduckgo.networkprotection.store.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vpn_flagged_auto_excluded_apps")
data class FlaggedIncompatibleApp(
    @PrimaryKey val packageName: String,
)

@Entity(tableName = "vpn_auto_excluded_apps")
data class VpnIncompatibleApp(
    @PrimaryKey val packageName: String,
)
