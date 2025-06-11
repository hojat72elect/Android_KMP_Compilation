

package com.duckduckgo.remote.messaging.impl

import com.duckduckgo.remote.messaging.api.RemoteMessagingRepository
import com.duckduckgo.remote.messaging.impl.mappers.RemoteMessagingConfigJsonMapper
import com.duckduckgo.remote.messaging.impl.models.JsonRemoteMessagingConfig
import com.duckduckgo.remote.messaging.store.RemoteMessagingConfig
import com.duckduckgo.remote.messaging.store.RemoteMessagingConfigRepository
import com.duckduckgo.remote.messaging.store.expired
import com.duckduckgo.remote.messaging.store.invalidated
import timber.log.Timber

interface RemoteMessagingConfigProcessor {
    suspend fun process(jsonRemoteMessagingConfig: JsonRemoteMessagingConfig)
}

class RealRemoteMessagingConfigProcessor(
    private val remoteMessagingConfigJsonMapper: RemoteMessagingConfigJsonMapper,
    private val remoteMessagingConfigRepository: RemoteMessagingConfigRepository,
    private val remoteMessagingRepository: RemoteMessagingRepository,
    private val remoteMessagingConfigMatcher: RemoteMessagingConfigMatcher,
) : RemoteMessagingConfigProcessor {

    override suspend fun process(jsonRemoteMessagingConfig: JsonRemoteMessagingConfig) {
        Timber.v("RMF: process ${jsonRemoteMessagingConfig.version}")
        val currentConfig = remoteMessagingConfigRepository.get()
        val currentVersion = currentConfig.version
        val newVersion = jsonRemoteMessagingConfig.version

        val isNewVersion = currentVersion != newVersion
        val shouldProcess = currentConfig.invalidated() || currentConfig.expired()

        if (isNewVersion || shouldProcess) {
            val config = remoteMessagingConfigJsonMapper.map(jsonRemoteMessagingConfig)
            val message = remoteMessagingConfigMatcher.evaluate(config)
            remoteMessagingConfigRepository.insert(RemoteMessagingConfig(version = jsonRemoteMessagingConfig.version))
            remoteMessagingRepository.activeMessage(message)
        } else {
            Timber.v("RMF: skip")
        }
    }
}
