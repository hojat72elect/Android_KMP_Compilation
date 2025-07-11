

package com.duckduckgo.fingerprintprotection.store.features.fingerprintingcanvas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.fingerprintprotection.store.FingerprintingCanvasEntity

@Dao
abstract class FingerprintingCanvasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(fingerprintingCanvasEntity: FingerprintingCanvasEntity)

    @Transaction
    open fun updateAll(
        fingerprintingCanvasEntity: FingerprintingCanvasEntity,
    ) {
        delete()
        insert(fingerprintingCanvasEntity)
    }

    @Query("select * from fingerprinting_canvas")
    abstract fun get(): FingerprintingCanvasEntity?

    @Query("delete from fingerprinting_canvas")
    abstract fun delete()
}
