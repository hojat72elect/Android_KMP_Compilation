

package com.duckduckgo.webcompat.store

import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface WebCompatRepository {
    fun updateAll(
        webCompatEntity: WebCompatEntity,
    )
    fun getWebCompatEntity(): WebCompatEntity
}

class RealWebCompatRepository constructor(
    database: WebCompatDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : WebCompatRepository {

    private val webCompatDao: WebCompatDao = database.webCompatDao()
    private var webCompatEntity = WebCompatEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(webCompatEntity: WebCompatEntity) {
        webCompatDao.updateAll(webCompatEntity)
        loadToMemory()
    }

    override fun getWebCompatEntity(): WebCompatEntity {
        return webCompatEntity
    }

    private fun loadToMemory() {
        webCompatEntity =
            webCompatDao.get() ?: WebCompatEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
