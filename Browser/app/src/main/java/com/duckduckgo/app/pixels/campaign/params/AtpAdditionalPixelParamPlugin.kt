

package com.duckduckgo.app.pixels.campaign.params

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.app.tracking.AppTrackingProtection
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class AtpEnabledAdditionalPixelParamPlugin @Inject constructor(
    private val appTrackingProtection: AppTrackingProtection,
) : AdditionalPixelParamPlugin {
    override suspend fun params(): Pair<String, String> = Pair(
        "atpOnboarded",
        "${appTrackingProtection.isOnboarded()}",
    )
}
