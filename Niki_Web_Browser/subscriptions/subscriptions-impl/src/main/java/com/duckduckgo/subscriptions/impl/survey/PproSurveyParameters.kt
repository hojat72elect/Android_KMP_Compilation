

package com.duckduckgo.subscriptions.impl.survey

import com.duckduckgo.common.utils.CurrentTimeProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.subscriptions.impl.SubscriptionsManager
import com.duckduckgo.survey.api.SurveyParameterPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class PproPurchasePlatformSurveyParameterPlugin @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
) : SurveyParameterPlugin {
    override val surveyParamKey: String = "ppro_platform"

    override suspend fun evaluate(): String = subscriptionsManager.getSubscription()?.platform?.lowercase() ?: ""
}

@ContributesMultibinding(AppScope::class)
class PproBillingParameterPlugin @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
) : SurveyParameterPlugin {
    override val surveyParamKey: String = "ppro_billing"

    override suspend fun evaluate(): String {
        return subscriptionsManager.getSubscription()?.billingPeriod ?: ""
    }
}

@ContributesMultibinding(AppScope::class)
class PproDaysSincePurchaseSurveyParameterPlugin @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
    private val currentTimeProvider: CurrentTimeProvider,
) : SurveyParameterPlugin {
    override val surveyParamKey: String = "ppro_days_since_purchase"

    override suspend fun evaluate(): String {
        val startedAt = subscriptionsManager.getSubscription()?.startedAt
        return startedAt?.let {
            "${TimeUnit.MILLISECONDS.toDays(currentTimeProvider.currentTimeMillis() - it)}"
        } ?: "0"
    }
}

@ContributesMultibinding(AppScope::class)
class PproDaysUntilExpirySurveyParameterPlugin @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
    private val currentTimeProvider: CurrentTimeProvider,
) : SurveyParameterPlugin {
    override val surveyParamKey: String = "ppro_days_until_exp"

    override suspend fun evaluate(): String {
        val expiry = subscriptionsManager.getSubscription()?.expiresOrRenewsAt
        return expiry?.let {
            "${TimeUnit.MILLISECONDS.toDays(expiry - currentTimeProvider.currentTimeMillis())}"
        } ?: "0"
    }
}

@ContributesMultibinding(AppScope::class)
class PproStatusParameterPlugin @Inject constructor(
    private val subscriptionsManager: SubscriptionsManager,
) : SurveyParameterPlugin {
    private val invalidCharRegex = Regex("[ -]")
    override val surveyParamKey: String = "ppro_status"

    override suspend fun evaluate(): String =
        subscriptionsManager.getSubscription()?.status?.statusName?.lowercase()?.replace(invalidCharRegex, "_") ?: ""
}
