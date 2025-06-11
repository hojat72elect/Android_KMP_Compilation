

package com.duckduckgo.app.survey.rmf

import com.duckduckgo.app.browser.senseofprotection.SenseOfProtectionToggles
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.survey.api.SurveyParameterPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

// Added while senseOfProtectionNewUserExperimentApr25 is active
@ContributesMultibinding(AppScope::class)
class SenseOfProtectionCohortSurveyParameterPlugin @Inject constructor(
    private val senseOfProtectionToggles: SenseOfProtectionToggles,
) : SurveyParameterPlugin {
    override val surveyParamKey: String = "senseProtectionCohort"

    override suspend fun evaluate(): String = senseOfProtectionToggles.senseOfProtectionNewUserExperimentMay25().getCohort()?.name.orEmpty()
}
