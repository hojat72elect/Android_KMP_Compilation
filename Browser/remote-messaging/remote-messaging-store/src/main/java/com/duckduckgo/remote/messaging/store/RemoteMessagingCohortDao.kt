

package com.duckduckgo.remote.messaging.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteMessagingCohortDao {
    @Query("select * from remote_messaging_cohort where messageId = :messageID limit 1")
    suspend fun messageById(messageID: String): RemoteMessagingCohort?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RemoteMessagingCohort)
}
