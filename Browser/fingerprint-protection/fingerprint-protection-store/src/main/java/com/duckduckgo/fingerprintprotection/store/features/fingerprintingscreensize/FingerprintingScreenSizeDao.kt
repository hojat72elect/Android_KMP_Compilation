

package com.duckduckgo.fingerprintprotection.store.features.fingerprintingscreensize

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.fingerprintprotection.store.FingerprintingScreenSizeEntity

@Dao
abstract class FingerprintingScreenSizeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(fingerprintingScreenSizeEntity: FingerprintingScreenSizeEntity)

    @Transaction
    open fun updateAll(
        fingerprintingScreenSizeEntity: FingerprintingScreenSizeEntity,
    ) {
        delete()
        insert(fingerprintingScreenSizeEntity)
    }

    @Query("select * from fingerprinting_screen_size")
    abstract fun get(): FingerprintingScreenSizeEntity?

    @Query("delete from fingerprinting_screen_size")
    abstract fun delete()
}
