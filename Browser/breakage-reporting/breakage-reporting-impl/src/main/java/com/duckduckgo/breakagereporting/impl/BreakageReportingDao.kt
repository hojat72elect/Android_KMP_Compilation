

package com.duckduckgo.breakagereporting.impl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BreakageReportingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(breakageReportingEntity: BreakageReportingEntity)

    @Transaction
    fun updateAll(
        breakageReportingEntity: BreakageReportingEntity,
    ) {
        delete()
        insert(breakageReportingEntity)
    }

    @Query("select * from breakage_reporting")
    fun get(): BreakageReportingEntity?

    @Query("delete from breakage_reporting")
    fun delete()
}
