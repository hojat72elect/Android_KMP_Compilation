

package com.duckduckgo.feature.toggles.impl.rmf

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureTogglesInventory
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.JsonMatchingAttribute
import com.duckduckgo.remote.messaging.api.JsonToMatchingAttributeMapper
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = JsonToMatchingAttributeMapper::class,
)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = AttributeMatcherPlugin::class,
)
@SingleInstanceIn(AppScope::class)
class RMFToggleMatchingAttributePlugin @Inject constructor(
    private val featureTogglesInventory: FeatureTogglesInventory,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {

    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? = if (key == ToggleMatchingAttribute.KEY) {
        val value = jsonMatchingAttribute.value as? List<String>
        value.takeUnless { it.isNullOrEmpty() }?.let { featureFlags ->
            ToggleMatchingAttribute(featureFlags)
        }
    } else {
        null
    }

    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is ToggleMatchingAttribute -> {
                assert(matchingAttribute.values.isNotEmpty())
                val flags = featureTogglesInventory.getAll()
                return matchingAttribute.values.map { attribute ->
                    val foundFlag = flags.find { it.featureName().name == attribute }
                    if (foundFlag == null) {
                        return false
                    }
                    foundFlag
                }.all {
                    it.isEnabled()
                }
            }

            else -> null
        }
    }
}

data class ToggleMatchingAttribute(
    val values: List<String>,
) : MatchingAttribute {
    companion object {
        const val KEY = "allFeatureFlagsEnabled"
    }
}
