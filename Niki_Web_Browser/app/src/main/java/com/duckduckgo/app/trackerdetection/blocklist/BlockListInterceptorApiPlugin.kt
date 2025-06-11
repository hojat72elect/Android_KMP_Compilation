

package com.duckduckgo.app.trackerdetection.blocklist

import com.duckduckgo.app.global.api.ApiInterceptorPlugin
import com.duckduckgo.app.pixels.AppPixelName.BLOCKLIST_TDS_FAILURE
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.app.trackerdetection.api.TDS_BASE_URL
import com.duckduckgo.app.trackerdetection.api.TdsRequired
import com.duckduckgo.app.trackerdetection.blocklist.BlockList.Cohorts.CONTROL
import com.duckduckgo.app.trackerdetection.blocklist.BlockList.Cohorts.TREATMENT
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureTogglesInventory
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import retrofit2.Invocation

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = ApiInterceptorPlugin::class,
)
class BlockListInterceptorApiPlugin @Inject constructor(
    private val inventory: FeatureTogglesInventory,
    private val moshi: Moshi,
    private val pixel: Pixel,
) : Interceptor, ApiInterceptorPlugin {

    private val jsonAdapter: JsonAdapter<Map<String, String>> by lazy {
        moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, String::class.java))
    }
    override fun intercept(chain: Chain): Response {
        val request = chain.request().newBuilder()

        val tdsRequired = chain.request().tag(Invocation::class.java)
            ?.method()
            ?.isAnnotationPresent(TdsRequired::class.java) == true

        return if (tdsRequired) {
            val activeExperiment = runBlocking {
                inventory.activeTdsFlag()
            }

            activeExperiment?.let {
                val config = activeExperiment.getSettings()?.let {
                    runCatching {
                        jsonAdapter.fromJson(it)
                    }.getOrDefault(emptyMap())
                } ?: emptyMap()
                val path = when {
                    activeExperiment.isEnabled(TREATMENT) -> config["treatmentUrl"]
                    activeExperiment.isEnabled(CONTROL) -> config["controlUrl"]
                    else -> config["nextUrl"]
                } ?: return chain.proceed(request.build())
                chain.proceed(request.url("$TDS_BASE_URL$path").build()).also { response ->
                    if (!response.isSuccessful) {
                        pixel.fire(BLOCKLIST_TDS_FAILURE, mapOf("code" to response.code.toString()))
                    }
                }
            } ?: chain.proceed(request.build())
        } else {
            chain.proceed(request.build())
        }
    }

    override fun getInterceptor(): Interceptor {
        return this
    }
}
