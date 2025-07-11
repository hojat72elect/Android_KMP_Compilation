

package com.duckduckgo.networkprotection.store.remote_config

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter

@Entity(tableName = "netp_config_toggles")
data class NetPConfigToggle(
    @PrimaryKey(autoGenerate = false) val name: String,
    val enabled: Boolean,
    val isManualOverride: Boolean = false,
    val localtime: String = DatabaseDateFormatter.timestamp(),
)
