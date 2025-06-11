

package com.duckduckgo.privacy.config.store.features.contentblocking

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.feature.toggles.api.FeatureException
import com.duckduckgo.privacy.config.store.ContentBlockingExceptionEntity
import com.duckduckgo.privacy.config.store.PrivacyConfigDatabase
import com.duckduckgo.privacy.config.store.toFeatureException
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface ContentBlockingRepository {
    fun updateAll(exceptions: List<ContentBlockingExceptionEntity>)
    val exceptions: CopyOnWriteArrayList<FeatureException>
}

class RealContentBlockingRepository(
    val database: PrivacyConfigDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : ContentBlockingRepository {

    private val contentBlockingDao: ContentBlockingDao = database.contentBlockingDao()
    override val exceptions = CopyOnWriteArrayList<FeatureException>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(exceptions: List<ContentBlockingExceptionEntity>) {
        contentBlockingDao.updateAll(exceptions)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        contentBlockingDao.getAll().map { exceptions.add(it.toFeatureException()) }
    }
}
