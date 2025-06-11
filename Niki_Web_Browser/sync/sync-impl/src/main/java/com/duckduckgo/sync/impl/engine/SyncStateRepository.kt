

package com.duckduckgo.sync.impl.engine

import com.duckduckgo.sync.store.dao.SyncAttemptDao
import com.duckduckgo.sync.store.model.SyncAttempt
import com.duckduckgo.sync.store.model.SyncAttemptState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

interface SyncStateRepository {

    fun state(): Flow<SyncAttempt?>

    fun store(syncAttempt: SyncAttempt)

    fun current(): SyncAttempt?

    fun updateSyncState(state: SyncAttemptState)

    fun clearAll()

    fun attempts(): List<SyncAttempt>
}

class AppSyncStateRepository @Inject constructor(private val syncAttemptDao: SyncAttemptDao) : SyncStateRepository {
    override fun state(): Flow<SyncAttempt?> {
        return syncAttemptDao.lastAttempt()
    }

    override fun store(syncAttempt: SyncAttempt) {
        syncAttemptDao.insert(syncAttempt)
    }

    override fun current(): SyncAttempt? {
        return syncAttemptDao.lastAttemptSync()
    }

    override fun updateSyncState(state: SyncAttemptState) {
        val last = syncAttemptDao.lastAttemptSync()
        if (last != null) {
            val updated = last.copy(state = state)
            Timber.d("Sync-State: updating sync attempt to $updated")
            syncAttemptDao.insert(updated)
        }
    }

    override fun clearAll() {
        syncAttemptDao.clear()
    }

    override fun attempts(): List<SyncAttempt> {
        return syncAttemptDao.allAttempts()
    }
}
