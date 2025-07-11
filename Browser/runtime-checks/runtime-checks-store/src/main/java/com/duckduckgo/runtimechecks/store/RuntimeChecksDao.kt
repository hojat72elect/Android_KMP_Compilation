

package com.duckduckgo.runtimechecks.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RuntimeChecksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(runtimeChecksEntity: RuntimeChecksEntity)

    @Transaction
    fun updateAll(
        runtimeChecksEntity: RuntimeChecksEntity,
    ) {
        delete()
        insert(runtimeChecksEntity)
    }

    @Query("select * from runtime_checks")
    fun get(): RuntimeChecksEntity?

    @Query("delete from runtime_checks")
    fun delete()
}
