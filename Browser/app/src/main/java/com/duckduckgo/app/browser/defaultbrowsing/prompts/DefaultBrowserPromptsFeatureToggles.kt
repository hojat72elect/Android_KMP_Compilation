

package com.duckduckgo.app.browser.defaultbrowsing.prompts

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.feature.toggles.api.Toggle.State.CohortName

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "defaultBrowserPrompts",
)
interface DefaultBrowserPromptsFeatureToggles {

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun defaultBrowserAdditionalPrompts202501(): Toggle

    enum class AdditionalPromptsCohortName(override val cohortName: String) : CohortName {
        CONTROL("control"),
        VARIANT_1("variant1"),
        VARIANT_2("variant2"),
        VARIANT_3("variant3"),
    }
}
