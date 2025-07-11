

package com.duckduckgo.daxprompts.impl

import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.daxprompts.impl.ReactivateUsersToggles.Cohorts.CONTROL
import com.duckduckgo.daxprompts.impl.ReactivateUsersToggles.Cohorts.VARIANT_BROWSER_PROMPT
import com.duckduckgo.daxprompts.impl.ReactivateUsersToggles.Cohorts.VARIANT_DUCKPLAYER_PROMPT
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.MetricsPixel
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface ReactivateUsersExperiment {

    suspend fun isControl(): Boolean
    suspend fun isDuckPlayerPrompt(): Boolean
    suspend fun isBrowserComparisonPrompt(): Boolean

    suspend fun fireDuckPlayerUseIfInExperiment()
    suspend fun fireSetBrowserAsDefault()
    suspend fun fireDuckPlayerClick()
    suspend fun fireChooseYourBrowserClick()
    suspend fun fireCloseScreen()
    suspend fun firePlusEvenMoreProtectionsLinkClick()
}

@ContributesBinding(
    scope = AppScope::class,
    boundType = ReactivateUsersExperiment::class,
)
@SingleInstanceIn(AppScope::class)
class ReactivateUsersExperimentImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val reactivateUsersToggles: ReactivateUsersToggles,
    private val reactivateUsersPixelsPlugin: ReactivateUsersPixelsPlugin,
    private val pixel: Pixel,
) : ReactivateUsersExperiment {

    override suspend fun isControl(): Boolean =
        reactivateUsersToggles.reactivateUsersExperimentMay25().isEnabled(CONTROL)

    override suspend fun isDuckPlayerPrompt(): Boolean =
        reactivateUsersToggles.reactivateUsersExperimentMay25().isEnabled(VARIANT_DUCKPLAYER_PROMPT)

    override suspend fun isBrowserComparisonPrompt(): Boolean =
        reactivateUsersToggles.reactivateUsersExperimentMay25().isEnabled(VARIANT_BROWSER_PROMPT)

    override suspend fun fireDuckPlayerUseIfInExperiment() {
        withContext(dispatcherProvider.io()) {
            if (reactivateUsersToggles.reactivateUsersExperimentMay25().isEnrolledAndEnabled(VARIANT_DUCKPLAYER_PROMPT)) {
                reactivateUsersPixelsPlugin.getDuckPlayerUseMetric()?.fire()
            }
        }
    }

    override suspend fun fireSetBrowserAsDefault() {
        withContext(dispatcherProvider.io()) {
            reactivateUsersPixelsPlugin.getSetBrowserAsDefaultMetric()?.fire()
        }
    }

    override suspend fun fireDuckPlayerClick() {
        withContext(dispatcherProvider.io()) {
            reactivateUsersPixelsPlugin.getDuckPlayerClickMetric()?.fire()
        }
    }

    override suspend fun fireChooseYourBrowserClick() {
        withContext(dispatcherProvider.io()) {
            reactivateUsersPixelsPlugin.getChooseYourBrowserClickMetric()?.fire()
        }
    }

    override suspend fun fireCloseScreen() {
        withContext(dispatcherProvider.io()) {
            reactivateUsersPixelsPlugin.getCloseScreenMetric()?.fire()
        }
    }

    override suspend fun firePlusEvenMoreProtectionsLinkClick() {
        withContext(dispatcherProvider.io()) {
            reactivateUsersPixelsPlugin.getPlusEvenMoreProtectionsLinkClickMetric()?.fire()
        }
    }

    private fun MetricsPixel.fire() = getPixelDefinitions().forEach {
        pixel.fire(it.pixelName, it.params)
    }
}
