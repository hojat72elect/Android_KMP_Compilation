package com.duckduckgo.autoconsent.impl.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class AutoconsentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDisabledCmps(disabledCmps: List<DisabledCmpsEntity>)

    @Transaction
    open fun updateAllDisabledCMPs(disabledCMPs: List<DisabledCmpsEntity>) {
        deleteDisabledCmps()
        insertDisabledCmps(disabledCMPs)
    }

    @Query("select * from autoconsent_disabled_cmps")
    abstract fun getDisabledCmps(): List<DisabledCmpsEntity>

    @Query("delete from autoconsent_disabled_cmps")
    abstract fun deleteDisabledCmps()
}
