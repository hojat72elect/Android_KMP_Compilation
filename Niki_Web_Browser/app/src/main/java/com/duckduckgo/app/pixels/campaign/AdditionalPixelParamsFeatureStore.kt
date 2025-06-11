

package com.duckduckgo.app.pixels.campaign

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(AdditionalPixelParamsFeature::class)
class AdditionalPproPixelParamFeatureStore @Inject constructor(
    private val additionalPixelParamsDataStore: AdditionalPixelParamsDataStore,
) : FeatureSettings.Store {

    private val jsonAdapter by lazy { buildJsonAdapter() }

    override fun store(jsonString: String) {
        jsonAdapter.fromJson(jsonString)?.let {
            additionalPixelParamsDataStore.includedOrigins = it.origins
        }
    }

    private fun buildJsonAdapter(): JsonAdapter<AdditionalPixelParamsSettings> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(AdditionalPixelParamsSettings::class.java)
    }
}

data class AdditionalPixelParamsSettings(
    val origins: List<String>,
)
