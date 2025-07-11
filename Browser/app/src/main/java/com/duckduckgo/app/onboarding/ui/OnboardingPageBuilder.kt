

package com.duckduckgo.app.onboarding.ui

import com.duckduckgo.app.onboarding.ui.page.DefaultBrowserPage
import com.duckduckgo.app.onboarding.ui.page.WelcomePage

interface OnboardingPageBuilder {
    fun buildExperimentWelcomePage(): WelcomePage
    fun buildDefaultBrowserPage(): DefaultBrowserPage

    sealed class OnboardingPageBlueprint {
        data object DefaultBrowserBlueprint : OnboardingPageBlueprint()
        data object ExperimentWelcomeBluePrint : OnboardingPageBlueprint()
    }
}

class OnboardingFragmentPageBuilder : OnboardingPageBuilder {

    override fun buildExperimentWelcomePage() = WelcomePage()
    override fun buildDefaultBrowserPage() = DefaultBrowserPage()
}
