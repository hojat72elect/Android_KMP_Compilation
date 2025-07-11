package com.duckduckgo.adclick.impl.remoteconfig

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(AdClickAttributionFeature::class)
class AdClickAttributionFeatureSettingsStore @Inject constructor(
    private val adClickAttributionRepository: AdClickAttributionRepository,
) : FeatureSettings.Store {

    private val jsonAdapter by lazy { buildJsonAdapter() }

    override fun store(jsonString: String) {
        jsonAdapter.fromJson(jsonString)?.let {
            adClickAttributionRepository.updateAll(
                it.linkFormats,
                it.allowlist,
                it.navigationExpiration,
                it.totalExpiration,
                it.heuristicDetection,
                it.domainDetection,
            )
        }
    }

    private fun buildJsonAdapter(): JsonAdapter<AdClickAttributionFeatureModel> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(AdClickAttributionFeatureModel::class.java)
    }
}
