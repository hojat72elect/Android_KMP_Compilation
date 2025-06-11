

package com.duckduckgo.history.impl.store

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import java.time.LocalDateTime

@Dao
interface HistoryDao {
    @Transaction
    @Query("SELECT * FROM history_entries")
    suspend fun getHistoryEntriesWithVisits(): List<HistoryEntryWithVisits>

    @Query("UPDATE history_entries SET title = :title WHERE id = :id")
    suspend fun updateTitle(id: Long, title: String)

    @Transaction
    suspend fun updateOrInsertVisit(url: String, title: String, query: String?, isSerp: Boolean, date: LocalDateTime) {
        val existingHistoryEntry = getHistoryEntryByUrl(url)

        if (existingHistoryEntry != null) {
            if (title.isNotBlank() && title != existingHistoryEntry.title) {
                updateTitle(existingHistoryEntry.id, title)
            }
            val newVisit = VisitEntity(timestamp = DatabaseDateFormatter.timestamp(date), historyEntryId = existingHistoryEntry.id)
            insertVisit(newVisit)
        } else {
            val newHistoryEntry = HistoryEntryEntity(url = url, title = title, query = query, isSerp = isSerp)
            val historyEntryId = insertHistoryEntry(newHistoryEntry)

            val newVisit = VisitEntity(timestamp = DatabaseDateFormatter.timestamp(date), historyEntryId = historyEntryId)
            insertVisit(newVisit)
        }
    }

    @Query("SELECT * FROM history_entries WHERE url = :url LIMIT 1")
    suspend fun getHistoryEntryByUrl(url: String): HistoryEntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryEntry(historyEntry: HistoryEntryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitEntity)

    @Query("DELETE FROM visits_list")
    suspend fun deleteAllVisits()

    @Query("DELETE FROM history_entries")
    suspend fun deleteAllHistoryEntries()

    @Transaction
    suspend fun deleteAll() {
        deleteAllVisits()
        deleteAllHistoryEntries()
    }

    @Delete
    suspend fun delete(entryEntity: HistoryEntryEntity)

    @Delete
    suspend fun delete(entities: List<HistoryEntryEntity>)

    @Query("DELETE FROM history_entries WHERE `query` = :query")
    suspend fun deleteEntriesByQuery(query: String)

    @Query("DELETE FROM history_entries WHERE `url` = :url")
    suspend fun deleteEntriesByUrl(url: String)

    @Query("DELETE FROM visits_list WHERE timestamp < :timestamp")
    suspend fun deleteOldVisitsByTimestamp(timestamp: String)

    @Query("DELETE FROM history_entries WHERE id NOT IN (SELECT DISTINCT historyEntryId FROM visits_list)")
    suspend fun deleteEntriesWithNoVisits()

    @Transaction
    suspend fun deleteEntriesOlderThan(dateTime: LocalDateTime) {
        val timestamp = DatabaseDateFormatter.timestamp(dateTime)

        deleteOldVisitsByTimestamp(timestamp)

        deleteEntriesWithNoVisits()
    }
}
