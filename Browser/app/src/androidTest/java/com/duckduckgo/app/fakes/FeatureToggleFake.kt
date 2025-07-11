

package com.duckduckgo.app.fakes

import com.duckduckgo.feature.toggles.api.FeatureToggle

class FeatureToggleFake : FeatureToggle {
    override fun isFeatureEnabled(
        featureName: String,
        defaultValue: Boolean,
    ): Boolean = true
}
