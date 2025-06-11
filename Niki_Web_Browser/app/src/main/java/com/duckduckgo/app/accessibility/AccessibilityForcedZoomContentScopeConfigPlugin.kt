

package com.duckduckgo.app.accessibility

import com.duckduckgo.app.accessibility.data.AccessibilitySettingsDataStore
import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class AccessibilityForcedZoomContentScopeConfigPlugin @Inject constructor(
    private val accessibilitySettingsDataStore: AccessibilitySettingsDataStore,
) : ContentScopeConfigPlugin {
    override fun config(): String = ""

    override fun preferences(): String? {
        return "\"forcedZoomEnabled\":${accessibilitySettingsDataStore.forceZoom}"
    }
}
