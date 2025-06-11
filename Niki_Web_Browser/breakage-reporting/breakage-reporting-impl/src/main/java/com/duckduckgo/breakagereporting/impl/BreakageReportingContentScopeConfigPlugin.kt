

package com.duckduckgo.breakagereporting.impl

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class BreakageReportingContentScopeConfigPlugin @Inject constructor(
    private val breakageReportingRepository: BreakageReportingRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = BreakageReportingFeatureName.BreakageReporting.value
        val config = breakageReportingRepository.getBreakageReportingEntity().json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
