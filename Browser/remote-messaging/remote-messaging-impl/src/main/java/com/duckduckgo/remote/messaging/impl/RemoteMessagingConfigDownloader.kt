

package com.duckduckgo.remote.messaging.impl

import com.duckduckgo.remote.messaging.impl.network.RemoteMessagingService
import timber.log.Timber

interface RemoteMessagingConfigDownloader {
    suspend fun download(): Boolean
}

class RealRemoteMessagingConfigDownloader constructor(
    private val remoteConfig: RemoteMessagingService,
    private val remoteMessagingConfigProcessor: RemoteMessagingConfigProcessor,
) : RemoteMessagingConfigDownloader {
    override suspend fun download(): Boolean {
        val response = kotlin.runCatching {
            Timber.v("RMF: downloading config")
            remoteConfig.config()
        }.onSuccess {
            remoteMessagingConfigProcessor.process(it)
        }.onFailure {
            Timber.e("RMF: error at RealRemoteMessagingConfigDownloader, %s", it.localizedMessage)
        }

        return response.isSuccess
    }
}
