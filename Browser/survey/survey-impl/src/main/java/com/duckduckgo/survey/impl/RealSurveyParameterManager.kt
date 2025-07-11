

package com.duckduckgo.survey.impl

import androidx.core.net.toUri
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.survey.api.SurveyParameterManager
import com.duckduckgo.survey.api.SurveyParameterPlugin
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealSurveyParameterManager @Inject constructor(
    private val surveyParameterPluginPoint: PluginPoint<SurveyParameterPlugin>,
) : SurveyParameterManager {

    override suspend fun buildSurveyUrlStrict(
        baseUrl: String,
        requestedQueryParams: List<String>,
    ): String? {
        val urlBuilder = baseUrl.toUri().buildUpon()
        requestedQueryParams.forEach { param ->
            surveyParameterPluginPoint.getProviderForSurveyParamKey(param)?.let {
                urlBuilder.appendQueryParameter(param, it.evaluate())
            } ?: return null
        }
        return urlBuilder.build().toString()
    }

    override suspend fun buildSurveyUrl(
        baseUrl: String,
        requestedQueryParams: List<String>,
    ): String {
        val urlBuilder = baseUrl.toUri().buildUpon()
        requestedQueryParams.forEach {
            urlBuilder.appendQueryParameter(it, surveyParameterPluginPoint.getProviderForSurveyParamKey(it)?.evaluate() ?: "")
        }
        return urlBuilder.build().toString()
    }
}
