

package com.duckduckgo.request.filterer.store

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.feature.toggles.api.FeatureException
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface RequestFiltererRepository {
    fun updateAll(exceptions: List<RequestFiltererExceptionEntity>, settings: SettingsEntity)
    var settings: SettingsEntity
    val exceptions: List<FeatureException>
}

class RealRequestFiltererRepository(
    val database: RequestFiltererDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : RequestFiltererRepository {

    private val requestFiltererDao: RequestFiltererDao = database.requestFiltererDao()

    override val exceptions = CopyOnWriteArrayList<FeatureException>()
    override var settings = SettingsEntity(windowInMs = DEFAULT_WINDOW_IN_MS)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(
        exceptions: List<RequestFiltererExceptionEntity>,
        settings: SettingsEntity,
    ) {
        requestFiltererDao.updateAll(exceptions, settings)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        requestFiltererDao.getAllRequestFiltererExceptions().map {
            exceptions.add(it.toFeatureException())
        }
        settings = requestFiltererDao.getSettings() ?: SettingsEntity(windowInMs = DEFAULT_WINDOW_IN_MS)
    }

    companion object {
        const val DEFAULT_WINDOW_IN_MS = 200
    }
}
