

package com.duckduckgo.autofill.store.engagement

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
abstract class AutofillEngagementDao {

    @Upsert
    abstract suspend fun upsert(engagement: AutofillEngagementEntity)

    @Query("DELETE FROM autofill_engagement WHERE date < :date")
    abstract suspend fun deleteOlderThan(date: String)

    @Query("DELETE FROM autofill_engagement")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM autofill_engagement where date = :date")
    abstract suspend fun getEngagement(date: String): AutofillEngagementEntity?
}
