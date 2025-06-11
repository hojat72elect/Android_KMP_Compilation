

package com.duckduckgo.sync.settings.impl

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "settings_sync_meta")
data class SettingsSyncMetadataEntity(
    @PrimaryKey val key: String,
    var modified_at: String?, // should follow iso8601 format
    var deleted_at: String?, // should follow iso8601 format
)
