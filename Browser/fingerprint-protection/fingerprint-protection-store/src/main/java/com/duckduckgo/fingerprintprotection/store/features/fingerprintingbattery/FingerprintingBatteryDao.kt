

package com.duckduckgo.fingerprintprotection.store.features.fingerprintingbattery

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.fingerprintprotection.store.FingerprintingBatteryEntity

@Dao
abstract class FingerprintingBatteryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(fingerprintingBatteryEntity: FingerprintingBatteryEntity)

    @Transaction
    open fun updateAll(
        fingerprintingBatteryEntity: FingerprintingBatteryEntity,
    ) {
        delete()
        insert(fingerprintingBatteryEntity)
    }

    @Query("select * from fingerprinting_battery")
    abstract fun get(): FingerprintingBatteryEntity?

    @Query("delete from fingerprinting_battery")
    abstract fun delete()
}
