

package com.duckduckgo.elementhiding.store

import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface ElementHidingRepository {
    fun updateAll(
        elementHidingEntity: ElementHidingEntity,
    )
    var elementHidingEntity: ElementHidingEntity
}

class RealElementHidingRepository constructor(
    val database: ElementHidingDatabase,
    val coroutineScope: CoroutineScope,
    val dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : ElementHidingRepository {

    private val elementHidingDao: ElementHidingDao = database.elementHidingDao()
    override var elementHidingEntity = ElementHidingEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(elementHidingEntity: ElementHidingEntity) {
        elementHidingDao.updateAll(elementHidingEntity)
        loadToMemory()
    }

    private fun loadToMemory() {
        elementHidingEntity =
            elementHidingDao.get() ?: ElementHidingEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
