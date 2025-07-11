

package com.duckduckgo.settings.api

import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

interface SettingsPageFeature {

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @Toggle.InternalAlwaysEnabled
    fun self(): Toggle
}
