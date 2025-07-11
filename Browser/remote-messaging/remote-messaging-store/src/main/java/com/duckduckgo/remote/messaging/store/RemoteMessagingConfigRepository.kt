

package com.duckduckgo.remote.messaging.store

interface RemoteMessagingConfigRepository {
    fun insert(remoteMessagingConfig: RemoteMessagingConfig)
    fun get(): RemoteMessagingConfig
    fun invalidate()
}

class LocalRemoteMessagingConfigRepository(database: RemoteMessagingDatabase) : RemoteMessagingConfigRepository {

    private val dao: RemoteMessagingConfigDao = database.remoteMessagingConfigDao()

    override fun insert(remoteMessagingConfig: RemoteMessagingConfig) {
        dao.insert(remoteMessagingConfig)
    }

    override fun get(): RemoteMessagingConfig {
        return dao.get() ?: RemoteMessagingConfig(version = 0)
    }

    override fun invalidate() {
        return dao.invalidate()
    }
}
