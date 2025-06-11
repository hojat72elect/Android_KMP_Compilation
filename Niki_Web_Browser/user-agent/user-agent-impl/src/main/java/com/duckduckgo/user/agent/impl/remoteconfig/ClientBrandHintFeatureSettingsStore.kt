

package com.duckduckgo.user.agent.impl.remoteconfig

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject
import timber.log.Timber

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(ClientBrandHintFeature::class)
class ClientBrandHintFeatureSettingsStore @Inject constructor(
    private val repository: ClientBrandHintFeatureSettingsRepository,
) : FeatureSettings.Store {

    private val jsonAdapter by lazy { buildJsonAdapter() }

    override fun store(jsonString: String) {
        Timber.v("ClientBrandHintProvider: store $jsonString")
        jsonAdapter.fromJson(jsonString)?.let {
            repository.updateAllSettings(it)
        }
    }

    private fun buildJsonAdapter(): JsonAdapter<ClientBrandHintSettings> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(ClientBrandHintSettings::class.java)
    }
}
