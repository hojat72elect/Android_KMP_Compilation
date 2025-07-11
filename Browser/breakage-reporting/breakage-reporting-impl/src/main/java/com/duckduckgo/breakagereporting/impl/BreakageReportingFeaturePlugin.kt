

package com.duckduckgo.breakagereporting.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class BreakageReportingFeaturePlugin @Inject constructor(
    private val breakageReportingRepository: BreakageReportingRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val breakageReportingFeatureName = breakageReportingFeatureValueOf(featureName) ?: return false
        if (breakageReportingFeatureName.value == this.featureName) {
            val entity = BreakageReportingEntity(json = jsonString)
            breakageReportingRepository.updateAll(breakageReportingEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = BreakageReportingFeatureName.BreakageReporting.value
}
