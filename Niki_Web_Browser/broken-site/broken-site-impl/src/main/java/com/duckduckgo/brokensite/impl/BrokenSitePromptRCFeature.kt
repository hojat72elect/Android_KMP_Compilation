

package com.duckduckgo.brokensite.impl

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.feature.toggles.api.Toggle.DefaultValue
import com.duckduckgo.feature.toggles.api.Toggle.InternalAlwaysEnabled
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "brokenSitePrompt",
    settingsStore = BrokenSitePromptRCFeatureStore::class,
)
interface BrokenSitePromptRCFeature {
    @InternalAlwaysEnabled
    @DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
@RemoteFeatureStoreNamed(BrokenSitePromptRCFeature::class)
class BrokenSitePromptRCFeatureStore @Inject constructor(
    private val repository: BrokenSiteReportRepository,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
) : FeatureSettings.Store {

    private val jsonAdapter by lazy { buildJsonAdapter() }

    override fun store(jsonString: String) {
        appCoroutineScope.launch {
            jsonAdapter.fromJson(jsonString)?.let {
                repository.setBrokenSitePromptRCSettings(it.maxDismissStreak, it.dismissStreakResetDays, it.coolDownDays)
            }
        }
    }

    private fun buildJsonAdapter(): JsonAdapter<BrokenSitePromptSettings> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(BrokenSitePromptSettings::class.java)
    }

    data class BrokenSitePromptSettings(
        val maxDismissStreak: Int,
        val dismissStreakResetDays: Int,
        val coolDownDays: Int,
    )
}
