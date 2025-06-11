

package com.duckduckgo.pir.internal

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.feature.toggles.api.Toggle.DefaultValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "pir",
)
interface PirRemoteFeatures {
    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun self(): Toggle

    @DefaultValue(DefaultFeatureValue.FALSE)
    fun allowPirRun(): Toggle
}
