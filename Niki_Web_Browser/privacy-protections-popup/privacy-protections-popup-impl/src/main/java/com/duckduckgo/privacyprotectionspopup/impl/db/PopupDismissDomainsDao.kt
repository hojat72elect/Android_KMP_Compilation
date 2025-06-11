

package com.duckduckgo.privacyprotectionspopup.impl.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.Instant
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PopupDismissDomainsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: PopupDismissDomain)

    @Query("SELECT * FROM popup_dismiss_domains WHERE domain = :domain")
    abstract fun query(domain: String): Flow<PopupDismissDomain?>

    @Query("DELETE FROM popup_dismiss_domains WHERE dismissedAt < :time")
    abstract suspend fun removeEntriesOlderThan(time: Instant)

    @Query("DELETE FROM popup_dismiss_domains")
    abstract suspend fun removeAllEntries()
}
