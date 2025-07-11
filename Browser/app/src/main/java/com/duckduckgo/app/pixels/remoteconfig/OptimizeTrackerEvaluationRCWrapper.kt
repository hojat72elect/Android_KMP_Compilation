

package com.duckduckgo.app.pixels.remoteconfig

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface OptimizeTrackerEvaluationRCWrapper {
    val enabled: Boolean
}

@ContributesBinding(AppScope::class)
class RealOptimizeTrackerEvaluationRCWrapper @Inject constructor(
    private val androidBrowserConfigFeature: AndroidBrowserConfigFeature,
) : OptimizeTrackerEvaluationRCWrapper {
    override val enabled by lazy { androidBrowserConfigFeature.optimizeTrackerEvaluationV2().isEnabled() }
}
