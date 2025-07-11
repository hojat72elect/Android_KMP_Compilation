

package com.duckduckgo.history.impl

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.history.api.HistoryEntry
import com.duckduckgo.history.impl.store.HistoryDao
import com.duckduckgo.history.impl.store.HistoryDataStore
import java.time.LocalDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface HistoryRepository {
    fun getHistory(): Flow<List<HistoryEntry>>

    suspend fun saveToHistory(
        url: String,
        title: String?,
        query: String?,
        isSerp: Boolean,
    )

    suspend fun clearHistory()

    suspend fun removeHistoryEntryByUrl(url: String)

    suspend fun removeHistoryEntryByQuery(query: String)

    suspend fun isHistoryUserEnabled(default: Boolean): Boolean

    suspend fun setHistoryUserEnabled(value: Boolean)

    suspend fun clearEntriesOlderThan(dateTime: LocalDateTime)

    suspend fun hasHistory(): Boolean
}

class RealHistoryRepository(
    private val historyDao: HistoryDao,
    private val dispatcherProvider: DispatcherProvider,
    private val appCoroutineScope: CoroutineScope,
    private val historyDataStore: HistoryDataStore,
) : HistoryRepository {

    private var cachedHistoryEntries: List<HistoryEntry>? = null

    override fun getHistory(): Flow<List<HistoryEntry>> = runCatching {
        flow {
            emit(cachedHistoryEntries ?: fetchAndCacheHistoryEntries())
        }.flowOn(dispatcherProvider.io())
    }.getOrElse { flowOf(emptyList()) }

    override suspend fun saveToHistory(
        url: String,
        title: String?,
        query: String?,
        isSerp: Boolean,
    ) {
        withContext(dispatcherProvider.io()) {
            historyDao.updateOrInsertVisit(
                url,
                title ?: "",
                query,
                isSerp,
                LocalDateTime.now(),
            )
            fetchAndCacheHistoryEntries()
        }
    }

    override suspend fun clearHistory() {
        withContext(dispatcherProvider.io()) {
            cachedHistoryEntries = null
            historyDao.deleteAll()
            fetchAndCacheHistoryEntries()
        }
    }

    override suspend fun removeHistoryEntryByUrl(url: String) {
        withContext(dispatcherProvider.io()) {
            cachedHistoryEntries = null
            historyDao.deleteEntriesByUrl(url)
            fetchAndCacheHistoryEntries()
        }
    }

    override suspend fun removeHistoryEntryByQuery(query: String) {
        withContext(dispatcherProvider.io()) {
            cachedHistoryEntries = null
            historyDao.deleteEntriesByQuery(query)
            fetchAndCacheHistoryEntries()
        }
    }

    override suspend fun isHistoryUserEnabled(default: Boolean): Boolean {
        return withContext(dispatcherProvider.io()) {
            historyDataStore.isHistoryUserEnabled(default)
        }
    }

    override suspend fun setHistoryUserEnabled(value: Boolean) {
        withContext(dispatcherProvider.io()) {
            historyDataStore.setHistoryUserEnabled(value)
        }
    }

    private suspend fun fetchAndCacheHistoryEntries(): List<HistoryEntry> {
        return historyDao
            .getHistoryEntriesWithVisits()
            .mapNotNull { it.toHistoryEntry() }
            .also { cachedHistoryEntries = it }
    }

    override suspend fun clearEntriesOlderThan(dateTime: LocalDateTime) {
        cachedHistoryEntries = null
        historyDao.deleteEntriesOlderThan(dateTime)
        fetchAndCacheHistoryEntries()
    }

    override suspend fun hasHistory(): Boolean {
        return withContext(dispatcherProvider.io()) {
            (cachedHistoryEntries ?: fetchAndCacheHistoryEntries()).let {
                it.isNotEmpty()
            }
        }
    }
}
