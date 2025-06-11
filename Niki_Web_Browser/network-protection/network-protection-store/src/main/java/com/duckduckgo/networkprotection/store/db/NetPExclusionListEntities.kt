

package com.duckduckgo.networkprotection.store.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "netp_manual_app_exclusion_list")
data class NetPManuallyExcludedApp(
    @PrimaryKey val packageId: String,
    val isProtected: Boolean,
)
