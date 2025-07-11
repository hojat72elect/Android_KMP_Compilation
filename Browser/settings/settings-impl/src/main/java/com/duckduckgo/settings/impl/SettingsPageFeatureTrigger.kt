

package com.duckduckgo.settings.impl

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.settings.api.SettingsPageFeature

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "settingsPage",
    boundType = SettingsPageFeature::class,
)
private interface SettingsPageFeatureCodeGenTrigger
