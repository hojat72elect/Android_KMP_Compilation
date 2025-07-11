package com.duckduckgo.adclick.impl.store.exemptions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class AdClickExemptionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTabExemption(tabExemptionEntity: AdClickTabExemptionEntity)

    @Query("select * from tab_exemptions where tabId = :tabId")
    abstract fun getTabExemption(tabId: String): AdClickTabExemptionEntity?

    @Query("select * from tab_exemptions")
    abstract fun getAllTabExemptions(): List<AdClickTabExemptionEntity>

    @Query("delete from tab_exemptions where tabId = :tabId")
    abstract fun deleteTabExemption(tabId: String)

    @Query("delete from tab_exemptions")
    abstract fun deleteAllTabExemptions()

    @Query("delete from tab_exemptions where exemptionDeadline < :exemptionDeadline")
    abstract fun deleteAllExpiredTabExemptions(exemptionDeadline: Long)
}
