

package com.duckduckgo.runtimechecks.store

import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface RuntimeChecksRepository {
    fun updateAll(
        runtimeChecksEntity: RuntimeChecksEntity,
    )
    fun getRuntimeChecksEntity(): RuntimeChecksEntity
}

class RealRuntimeChecksRepository constructor(
    database: RuntimeChecksDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : RuntimeChecksRepository {

    private val runtimeChecksDao: RuntimeChecksDao = database.runtimeChecksDao()
    private var runtimeChecksEntity = RuntimeChecksEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(runtimeChecksEntity: RuntimeChecksEntity) {
        runtimeChecksDao.updateAll(runtimeChecksEntity)
        loadToMemory()
    }

    override fun getRuntimeChecksEntity(): RuntimeChecksEntity {
        return runtimeChecksEntity
    }

    private fun loadToMemory() {
        runtimeChecksEntity =
            runtimeChecksDao.get() ?: RuntimeChecksEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
