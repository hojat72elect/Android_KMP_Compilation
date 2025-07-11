

package com.duckduckgo.fingerprintprotection.store.features.fingerprintinghardware

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.fingerprintprotection.store.FingerprintingHardwareEntity

@Dao
abstract class FingerprintingHardwareDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(fingerprintingHardwareEntity: FingerprintingHardwareEntity)

    @Transaction
    open fun updateAll(
        fingerprintingHardwareEntity: FingerprintingHardwareEntity,
    ) {
        delete()
        insert(fingerprintingHardwareEntity)
    }

    @Query("select * from fingerprinting_hardware")
    abstract fun get(): FingerprintingHardwareEntity?

    @Query("delete from fingerprinting_hardware")
    abstract fun delete()
}
