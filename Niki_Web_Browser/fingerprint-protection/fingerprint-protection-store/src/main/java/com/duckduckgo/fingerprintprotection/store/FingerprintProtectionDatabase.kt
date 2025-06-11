

package com.duckduckgo.fingerprintprotection.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingbattery.FingerprintingBatteryDao
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingcanvas.FingerprintingCanvasDao
import com.duckduckgo.fingerprintprotection.store.features.fingerprintinghardware.FingerprintingHardwareDao
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingscreensize.FingerprintingScreenSizeDao
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingtemporarystorage.FingerprintingTemporaryStorageDao

@Database(
    exportSchema = true,
    version = 2,
    entities = [
        FingerprintingBatteryEntity::class,
        FingerprintingCanvasEntity::class,
        FingerprintingHardwareEntity::class,
        FingerprintingScreenSizeEntity::class,
        FingerprintingTemporaryStorageEntity::class,
    ],
)
abstract class FingerprintProtectionDatabase : RoomDatabase() {
    abstract fun fingerprintingBatteryDao(): FingerprintingBatteryDao
    abstract fun fingerprintingCanvasDao(): FingerprintingCanvasDao
    abstract fun fingerprintingHardwareDao(): FingerprintingHardwareDao
    abstract fun fingerprintingScreenSizeDao(): FingerprintingScreenSizeDao
    abstract fun fingerprintingTemporaryStorageDao(): FingerprintingTemporaryStorageDao
}
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS `fingerprint_protection_seed`")
    }
}

val ALL_MIGRATIONS = listOf(
    MIGRATION_1_2,
)
