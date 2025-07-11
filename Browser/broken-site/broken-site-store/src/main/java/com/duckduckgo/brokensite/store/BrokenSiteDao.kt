

package com.duckduckgo.brokensite.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class BrokenSiteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBrokenSiteReport(brokenSiteReportEntity: BrokenSiteLastSentReportEntity)

    @Query("select * from broken_site_last_sent_report where hostnameHashPrefix = :hostnameHashPrefix")
    abstract fun getBrokenSiteReport(hostnameHashPrefix: String): BrokenSiteLastSentReportEntity?

    @Query("delete from broken_site_last_sent_report where lastSentTimestamp < :expiryTime")
    abstract fun deleteAllExpiredReports(expiryTime: String)
}
