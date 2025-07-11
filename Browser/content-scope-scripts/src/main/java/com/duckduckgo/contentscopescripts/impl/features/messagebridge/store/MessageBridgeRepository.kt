package com.duckduckgo.contentscopescripts.impl.features.messagebridge.store

import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface MessageBridgeRepository {
    fun updateAll(
        messageBridgeEntity: MessageBridgeEntity,
    )

    var messageBridgeEntity: MessageBridgeEntity
}

class RealMessageBridgeRepository(
    val database: MessageBridgeDatabase,
    val coroutineScope: CoroutineScope,
    val dispatcherProvider: DispatcherProvider,
) : MessageBridgeRepository {

    private val messageBridgeDao: MessageBridgeDao = database.messageBridgeDao()
    override var messageBridgeEntity = MessageBridgeEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            loadToMemory()
        }
    }

    override fun updateAll(messageBridgeEntity: MessageBridgeEntity) {
        messageBridgeDao.updateAll(messageBridgeEntity)
        loadToMemory()
    }

    private fun loadToMemory() {
        messageBridgeEntity =
            messageBridgeDao.get() ?: MessageBridgeEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
