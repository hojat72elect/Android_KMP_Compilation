

package com.duckduckgo.privacy.config.store.features.trackerallowlist

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.privacy.config.store.PrivacyConfigDatabase
import com.duckduckgo.privacy.config.store.TrackerAllowlistEntity
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface TrackerAllowlistRepository {
    fun updateAll(exceptions: List<TrackerAllowlistEntity>)
    val exceptions: CopyOnWriteArrayList<TrackerAllowlistEntity>
}

class RealTrackerAllowlistRepository(
    database: PrivacyConfigDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : TrackerAllowlistRepository {

    private val trackerAllowlistDao: TrackerAllowlistDao = database.trackerAllowlistDao()
    override val exceptions = CopyOnWriteArrayList<TrackerAllowlistEntity>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(exceptions: List<TrackerAllowlistEntity>) {
        trackerAllowlistDao.updateAll(exceptions)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        trackerAllowlistDao.getAll().map { exceptions.add(it) }
    }
}
