

package com.duckduckgo.subscriptions.api

import com.duckduckgo.navigation.api.GlobalActivityStarter.ActivityParams
import com.duckduckgo.subscriptions.api.PrivacyProUnifiedFeedback.PrivacyProFeedbackSource

sealed class PrivacyProFeedbackScreens {
    data class PrivacyProFeedbackScreenWithParams(val feedbackSource: PrivacyProFeedbackSource) : ActivityParams

    data object GeneralPrivacyProFeedbackScreenNoParams : ActivityParams

    data class PrivacyProAppFeedbackScreenWithParams(
        val appName: String,
        val appPackageName: String,
    ) : ActivityParams
}
