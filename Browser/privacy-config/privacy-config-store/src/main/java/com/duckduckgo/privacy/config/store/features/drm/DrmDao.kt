

package com.duckduckgo.privacy.config.store.features.drm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.DrmExceptionEntity

@Dao
abstract class DrmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<DrmExceptionEntity>)

    @Transaction
    open fun updateAll(domains: List<DrmExceptionEntity>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select * from drm_exceptions")
    abstract fun getAll(): List<DrmExceptionEntity>

    @Query("delete from drm_exceptions")
    abstract fun deleteAll()
}
