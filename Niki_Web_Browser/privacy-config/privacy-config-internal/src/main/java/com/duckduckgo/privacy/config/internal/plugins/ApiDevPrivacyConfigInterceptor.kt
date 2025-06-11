

package com.duckduckgo.privacy.config.internal.plugins

import android.webkit.URLUtil
import com.duckduckgo.app.global.api.ApiInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PRIVACY_REMOTE_CONFIG_URL
import com.duckduckgo.privacy.config.internal.store.DevPrivacyConfigSettingsDataStore
import com.squareup.anvil.annotations.ContributesMultibinding
import java.net.URI
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
class ApiDevPrivacyConfigInterceptor @Inject constructor(
    private val devSettingsDataStore: DevPrivacyConfigSettingsDataStore,
) : ApiInterceptorPlugin, Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val url = chain.request().url
        val storedUrl = devSettingsDataStore.remotePrivacyConfigUrl
        val isCustomSettingEnabled = devSettingsDataStore.useCustomPrivacyConfigUrl
        val validHost = runCatching { URI(storedUrl).host.isNotEmpty() }.getOrDefault(false)
        val canUrlBeChanged = isCustomSettingEnabled && !storedUrl.isNullOrEmpty() && URLUtil.isValidUrl(storedUrl) && validHost

        if (url.toString().contains(PRIVACY_REMOTE_CONFIG_URL) && canUrlBeChanged) {
            request.url(storedUrl!!)
            return chain.proceed(request.build())
        }

        return chain.proceed(chain.request())
    }

    override fun getInterceptor(): Interceptor {
        return this
    }
}
