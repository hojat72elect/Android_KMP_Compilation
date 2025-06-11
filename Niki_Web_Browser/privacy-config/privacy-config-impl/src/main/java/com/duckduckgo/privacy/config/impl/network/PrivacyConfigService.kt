

package com.duckduckgo.privacy.config.impl.network

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PRIVACY_REMOTE_CONFIG_URL
import com.duckduckgo.privacy.config.impl.models.JsonPrivacyConfig
import retrofit2.Response
import retrofit2.http.GET

@ContributesServiceApi(AppScope::class)
interface PrivacyConfigService {
    @GET(PRIVACY_REMOTE_CONFIG_URL)
    suspend fun privacyConfig(): Response<JsonPrivacyConfig>
}
