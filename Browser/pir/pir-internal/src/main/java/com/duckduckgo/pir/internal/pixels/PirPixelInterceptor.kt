

package com.duckduckgo.pir.internal.pixels

import android.content.Context
import android.os.PowerManager
import android.util.Base64
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.plugins.pixel.PixelInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.pir.internal.store.PitTestingStore
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PixelInterceptorPlugin::class,
)
class PirPixelInterceptor @Inject constructor(
    private val context: Context,
    private val appBuildConfig: AppBuildConfig,
    private val networkInfoProvider: NetworkInfoProvider,
    private val testingStore: PitTestingStore,
) : PixelInterceptorPlugin, Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val pixel = chain.request().url.pathSegments.last()

        val url = if (pixel.startsWith(PIXEL_PREFIX) && !EXCEPTIONS.any { exception -> pixel.startsWith(exception) }) {
            chain.request().url.newBuilder()
                .addQueryParameter(
                    KEY_METADATA,
                    JSONObject()
                        .put("os", appBuildConfig.sdkInt)
                        .put("batteryOptimizations", (!isIgnoringBatteryOptimizations()).toString())
                        .put("man", appBuildConfig.manufacturer)
                        .put("networkInfo", networkInfoProvider.getCurrentNetworkInfo())
                        .put("testerId", testingStore.testerId ?: "UNKNOWN")
                        .toString().toByteArray().run {
                            Base64.encodeToString(this, Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE)
                        },
                )
                .build()
        } else {
            chain.request().url
        }

        return chain.proceed(request.url(url).build())
    }

    override fun getInterceptor(): Interceptor = this

    private fun isIgnoringBatteryOptimizations(): Boolean {
        return runCatching {
            context.packageName?.let {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                powerManager.isIgnoringBatteryOptimizations(context.packageName)
            } ?: false
        }.getOrDefault(false)
    }

    companion object {
        private const val KEY_METADATA = "metadata"
        private const val PIXEL_PREFIX = "pir_internal"
        private val EXCEPTIONS = emptyList<String>()
    }
}
