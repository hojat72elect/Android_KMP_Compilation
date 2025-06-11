

package com.duckduckgo.survey.api

interface SurveyParameterPlugin {
    val surveyParamKey: String

    suspend fun evaluate(): String
}
