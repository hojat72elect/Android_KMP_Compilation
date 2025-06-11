

package com.duckduckgo.remote.messaging.impl.network

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.remote.messaging.impl.models.JsonRemoteMessagingConfig
import retrofit2.http.GET

@ContributesServiceApi(AppScope::class)
interface RemoteMessagingService {
    @GET("https://staticcdn.duckduckgo.com/remotemessaging/config/v1/android-config.json")
    suspend fun config(): JsonRemoteMessagingConfig
}
