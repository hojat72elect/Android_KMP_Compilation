

package com.duckduckgo.fingerprintprotection.store.features.fingerprintingtemporarystorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.fingerprintprotection.store.FingerprintingTemporaryStorageEntity

@Dao
abstract class FingerprintingTemporaryStorageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(fingerprintingTemporaryStorageEntity: FingerprintingTemporaryStorageEntity)

    @Transaction
    open fun updateAll(
        fingerprintingTemporaryStorageEntity: FingerprintingTemporaryStorageEntity,
    ) {
        delete()
        insert(fingerprintingTemporaryStorageEntity)
    }

    @Query("select * from fingerprinting_temporary_storage")
    abstract fun get(): FingerprintingTemporaryStorageEntity?

    @Query("delete from fingerprinting_temporary_storage")
    abstract fun delete()
}
