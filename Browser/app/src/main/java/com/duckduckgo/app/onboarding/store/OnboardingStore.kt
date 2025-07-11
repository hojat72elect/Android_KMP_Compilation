

package com.duckduckgo.app.onboarding.store

import com.duckduckgo.app.cta.ui.DaxBubbleCta.DaxDialogIntroOption

interface OnboardingStore {
    var onboardingDialogJourney: String?
    var visitSiteCtaDisplayCount: Int

    @Deprecated(message = "Parameter used for a temporary pixel")
    fun getSearchOptions(): List<DaxDialogIntroOption>
    fun getSitesOptions(): List<DaxDialogIntroOption>
    fun getExperimentSearchOptions(): List<DaxDialogIntroOption>
    fun clearVisitSiteCtaDisplayCount()
}
