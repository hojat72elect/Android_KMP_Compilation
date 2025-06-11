package com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store

import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface NavigatorInterfaceRepository {
    fun updateAll(
        navigatorInterfaceEntity: NavigatorInterfaceEntity,
    )

    var navigatorInterfaceEntity: NavigatorInterfaceEntity
}

class RealNavigatorInterfaceRepository(
    val database: NavigatorInterfaceDatabase,
    val coroutineScope: CoroutineScope,
    val dispatcherProvider: DispatcherProvider,
) : NavigatorInterfaceRepository {

    private val navigatorInterfaceDao: NavigatorInterfaceDao = database.navigatorInterfaceDao()
    override var navigatorInterfaceEntity = NavigatorInterfaceEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            loadToMemory()
        }
    }

    override fun updateAll(navigatorInterfaceEntity: NavigatorInterfaceEntity) {
        navigatorInterfaceDao.updateAll(navigatorInterfaceEntity)
        loadToMemory()
    }

    private fun loadToMemory() {
        navigatorInterfaceEntity =
            navigatorInterfaceDao.get() ?: NavigatorInterfaceEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
