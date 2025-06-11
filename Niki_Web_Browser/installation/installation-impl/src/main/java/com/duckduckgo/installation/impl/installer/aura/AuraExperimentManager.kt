

package com.duckduckgo.installation.impl.installer.aura

import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.app.statistics.AtbInitializerListener
import com.duckduckgo.app.statistics.store.StatisticsDataStore
import com.duckduckgo.browser.api.referrer.AppReferrer
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.installation.impl.installer.InstallSourceExtractor
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext

@ContributesMultibinding(AppScope::class)
@PriorityKey(AtbInitializerListener.PRIORITY_AURA_EXPERIMENT_MANAGER)
@SingleInstanceIn(AppScope::class)
class AuraExperimentManager @Inject constructor(
    private val auraExperimentFeature: AuraExperimentFeature,
    private val auraExperimentListJsonParser: AuraExperimentListJsonParser,
    private val installSourceExtractor: InstallSourceExtractor,
    private val statisticsDataStore: StatisticsDataStore,
    private val appReferrer: AppReferrer,
    private val dispatcherProvider: DispatcherProvider,
) : AtbInitializerListener {

    override suspend fun beforeAtbInit() {
        initialize()
    }

    override fun beforeAtbInitTimeoutMillis(): Long = MAX_WAIT_TIME_MS

    private suspend fun initialize() = withContext(dispatcherProvider.io()) {
        if (auraExperimentFeature.self().isEnabled()) {
            installSourceExtractor.extract()?.let { source ->
                val settings = auraExperimentFeature.self().getSettings()
                val packages = auraExperimentListJsonParser.parseJson(settings).list
                if (packages.contains(source)) {
                    statisticsDataStore.variant = VARIANT
                    appReferrer.setOriginAttributeCampaign(ORIGIN)
                }
            }
        }
    }
    companion object {
        const val VARIANT = "mq"
        const val ORIGIN = "funnel_app_aurapaid_android"
        const val MAX_WAIT_TIME_MS = 1_500L
    }
}
