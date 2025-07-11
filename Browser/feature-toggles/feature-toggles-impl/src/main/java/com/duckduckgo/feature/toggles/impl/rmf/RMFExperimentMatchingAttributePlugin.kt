

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
class RMFExperimentMatchingAttributePlugin @Inject constructor(
    private val featureTogglesInventory: FeatureTogglesInventory,
) : JsonToMatchingAttributeMapper, AttributeMatcherPlugin {

    override fun map(
        key: String,
        jsonMatchingAttribute: JsonMatchingAttribute,
    ): MatchingAttribute? = if (key == ExperimentMatchingAttribute.KEY) {
        val value = jsonMatchingAttribute.value as? List<String>
        value.takeUnless { it.isNullOrEmpty() }?.let { featureFlags ->
            ExperimentMatchingAttribute(featureFlags)
        }
    } else {
        null
    }

    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is ExperimentMatchingAttribute -> {
                assert(matchingAttribute.values.isNotEmpty())
                val activeExperimentName = featureTogglesInventory.getAllActiveExperimentToggles().map { it.featureName().name }
                return matchingAttribute.values.any { activeExperimentName.contains(it) }
            }

            else -> null
        }
    }
}

data class ExperimentMatchingAttribute(
    val values: List<String>,
) : MatchingAttribute {
    companion object {
        const val KEY = "isUserInAnyActiveExperiment"
    }
}
