

package com.duckduckgo.autofill.store.targets

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "autofill_domain_target_apps_mapping")
data class DomainTargetAppEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val domain: String,
    @Embedded val targetApp: TargetApp,
    val dataExpiryInMillis: Long, // If from our dataset this will be 0L, else if part of user's cache, then a value greater than 0L.
)

data class TargetApp(
    @ColumnInfo(name = "app_package") val packageName: String,
    @ColumnInfo(name = "app_fingerprint") val sha256CertFingerprints: String,
)
