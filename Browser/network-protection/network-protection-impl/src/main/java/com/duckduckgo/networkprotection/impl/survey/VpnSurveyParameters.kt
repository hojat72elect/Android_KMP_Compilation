

package com.duckduckgo.networkprotection.impl.survey

import com.duckduckgo.common.utils.CurrentTimeProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.impl.cohort.NetpCohortStore
import com.duckduckgo.survey.api.SurveyParameterPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class VpnFirstUsedSurveyParameterPlugin @Inject constructor(
    private val netpCohortStore: NetpCohortStore,
    private val currentTimeProvider: CurrentTimeProvider,
) : SurveyParameterPlugin {
    override val surveyParamKey: String = "vpn_first_used"

    override suspend fun evaluate(): String {
        val now = currentTimeProvider.localDateTimeNow()
        val days = netpCohortStore.cohortLocalDate?.let { cohortLocalDate ->
            ChronoUnit.DAYS.between(cohortLocalDate, now)
        } ?: 0
        return "$days"
    }
}
