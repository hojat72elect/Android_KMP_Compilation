

package com.duckduckgo.autofill.impl.reporting

import com.duckduckgo.autofill.impl.reporting.remoteconfig.AutofillSiteBreakageReportingFeature
import com.duckduckgo.autofill.impl.reporting.remoteconfig.AutofillSiteBreakageReportingFeatureRepository
import com.duckduckgo.autofill.impl.time.TimeProvider
import com.duckduckgo.autofill.impl.urlmatcher.AutofillUrlMatcher
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface AutofillBreakageReportCanShowRules {
    suspend fun canShowForSite(url: String): Boolean
}

@ContributesBinding(AppScope::class)
class AutofillBreakageReportCanShowRulesImpl @Inject constructor(
    private val reportBreakageFeature: AutofillSiteBreakageReportingFeature,
    private val exceptionsRepository: AutofillSiteBreakageReportingFeatureRepository,
    private val dispatchers: DispatcherProvider,
    private val urlMatcher: AutofillUrlMatcher,
    private val dataStore: AutofillSiteBreakageReportingDataStore,
    private val timeProvider: TimeProvider,

) : AutofillBreakageReportCanShowRules {

    override suspend fun canShowForSite(url: String): Boolean {
        return withContext(dispatchers.io()) {
            if (!featureEnabledInRemoteConfig()) return@withContext false

            val eTldPlusOne = urlMatcher.extractUrlPartsForAutofill(url).eTldPlus1 ?: return@withContext false

            if (eTldPlusOne.isInExceptionList()) return@withContext false
            if (eTldPlusOne.siteReportSentRecently()) return@withContext false

            true
        }
    }

    private fun String.isInExceptionList() = exceptionsRepository.exceptions.contains(this)
    private fun featureEnabledInRemoteConfig() = reportBreakageFeature.self().isEnabled()
    private suspend fun String.siteReportSentRecently(): Boolean {
        val lastSubmissionForSite = dataStore.getTimestampLastFeedbackSent(this) ?: return false
        val minDaysBeforeResubmissionAllowed = dataStore.getMinimumNumberOfDaysBeforeReportPromptReshown()

        val timeSinceReportMs = timeProvider.currentTimeMillis() - lastSubmissionForSite
        return timeSinceReportMs <= TimeUnit.DAYS.toMillis(minDaysBeforeResubmissionAllowed.toLong())
    }
}
