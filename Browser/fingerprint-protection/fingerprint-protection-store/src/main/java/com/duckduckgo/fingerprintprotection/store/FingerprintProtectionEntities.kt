
package com.duckduckgo.fingerprintprotection.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fingerprinting_battery")
data class FingerprintingBatteryEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)

@Entity(tableName = "fingerprinting_canvas")
data class FingerprintingCanvasEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)

@Entity(tableName = "fingerprinting_hardware")
data class FingerprintingHardwareEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)

@Entity(tableName = "fingerprinting_screen_size")
data class FingerprintingScreenSizeEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)

@Entity(tableName = "fingerprinting_temporary_storage")
data class FingerprintingTemporaryStorageEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)

@Entity(tableName = "fingerprint_protection_seed")
data class FingerprintProtectionSeedEntity(
    @PrimaryKey val id: Int = 1,
    val seed: String,
)
