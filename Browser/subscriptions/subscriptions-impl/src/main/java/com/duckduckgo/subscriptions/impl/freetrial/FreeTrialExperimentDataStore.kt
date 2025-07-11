

package com.duckduckgo.subscriptions.impl.freetrial

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.PixelDefinition
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface FreeTrialExperimentDataStore {
    /**
     * @return number of times paywall has been displayed to user
     */
    var paywallImpressions: Int

    /**
     * Increases the count of paywall impressions
     */
    suspend fun increaseMetricForPaywallImpressions()

    /**
     * Returns the value [String] for the given pixel [definition]
     */
    suspend fun getMetricForPixelDefinition(definition: PixelDefinition): String?

    /**
     * Increases the count of paywall impressions for the given [definition]
     */
    suspend fun increaseMetricForPixelDefinition(
        definition: PixelDefinition,
        value: String,
    ): String?
}

@ContributesBinding(AppScope::class)
class FreeTrialExperimentDataStoreImpl @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
    private val dispatcherProvider: DispatcherProvider,
) : FreeTrialExperimentDataStore {

    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(
            FILENAME,
        )
    }

    override var paywallImpressions: Int
        get() = preferences.getInt(KEY_PAYWALL_IMPRESSIONS, 0)
        set(impressions) = preferences.edit { putInt(KEY_PAYWALL_IMPRESSIONS, impressions) }

    override suspend fun increaseMetricForPaywallImpressions() {
        withContext(dispatcherProvider.io()) {
            paywallImpressions += 1
        }
    }

    override suspend fun getMetricForPixelDefinition(definition: PixelDefinition): String? {
        val tag = "$definition"
        return withContext(dispatcherProvider.io()) {
            preferences.getString(tag, null)
        }
    }

    override suspend fun increaseMetricForPixelDefinition(
        definition: PixelDefinition,
        value: String,
    ): String? =
        withContext(dispatcherProvider.io()) {
            val tag = "$definition"
            preferences.edit(commit = true) {
                putString(tag, value)
            }
            preferences.getString(tag, null)
        }

    companion object {
        private const val FILENAME = "com.duckduckgo.subscriptions.freetrial.store"
        private const val KEY_PAYWALL_IMPRESSIONS = "PAYWALL_IMPRESSIONS"
    }
}
