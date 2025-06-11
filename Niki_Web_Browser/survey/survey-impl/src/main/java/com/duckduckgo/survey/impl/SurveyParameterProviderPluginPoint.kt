

package com.duckduckgo.survey.impl

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.survey.api.SurveyParameterPlugin

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = SurveyParameterPlugin::class,
)
@Suppress("unused")
private interface SurveyParameterProviderPluginPoint

fun PluginPoint<SurveyParameterPlugin>.getProviderForSurveyParamKey(key: String): SurveyParameterPlugin? {
    return runCatching { getPlugins().firstOrNull { it.surveyParamKey == key } }.getOrNull()
}
