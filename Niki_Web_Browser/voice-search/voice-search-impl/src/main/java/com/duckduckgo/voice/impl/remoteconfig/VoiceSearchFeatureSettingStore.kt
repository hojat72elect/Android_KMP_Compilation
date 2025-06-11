

package com.duckduckgo.voice.impl.remoteconfig

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(VoiceSearchFeature::class)
class VoiceSearchFeatureSettingStore @Inject constructor(
    private val voiceSearchFeatureRepository: VoiceSearchFeatureRepository,
) : FeatureSettings.Store {

    private val jsonAdapter by lazy { buildJsonAdapter() }

    override fun store(jsonSettings: String) {
        jsonAdapter.fromJson(jsonSettings)?.let {
            voiceSearchFeatureRepository.updateAllExceptions(it.excludedManufacturers, it.excludedLocales, it.minVersion)
        }
    }

    private fun buildJsonAdapter(): JsonAdapter<VoiceSearchSetting> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(VoiceSearchSetting::class.java)
    }
}
