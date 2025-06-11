

package com.duckduckgo.app.survey.api

data class SurveyGroup(
    val id: String,
    val surveyOptions: List<SurveyOption>,
) {

    data class SurveyOption(
        val url: String,
        val installationDay: Int?,
        val ratioOfUsersToShow: Double,
        val isEmailSignedInRequired: Boolean?,
        val isNetPOnboardedRequired: Boolean?,
        val daysSinceNetPEnabled: Int?,
        val urlParameters: List<String>?,
    )
}

sealed class SurveyUrlParameter(val parameter: String) {
    object EmailCohortParam : SurveyUrlParameter("cohort")
}
