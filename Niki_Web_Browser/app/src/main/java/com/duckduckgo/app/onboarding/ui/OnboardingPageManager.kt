

package com.duckduckgo.app.onboarding.ui

import com.duckduckgo.app.browser.defaultbrowsing.DefaultBrowserDetector
import com.duckduckgo.app.global.DefaultRoleBrowserDialog
import com.duckduckgo.app.onboarding.ui.OnboardingPageBuilder.OnboardingPageBlueprint
import com.duckduckgo.app.onboarding.ui.OnboardingPageBuilder.OnboardingPageBlueprint.DefaultBrowserBlueprint
import com.duckduckgo.app.onboarding.ui.OnboardingPageBuilder.OnboardingPageBlueprint.ExperimentWelcomeBluePrint
import com.duckduckgo.app.onboarding.ui.page.DefaultBrowserPage
import com.duckduckgo.app.onboarding.ui.page.OnboardingPageFragment
import com.duckduckgo.app.onboarding.ui.page.WelcomePage

interface OnboardingPageManager {
    fun pageCount(): Int
    fun buildPageBlueprints()
    fun buildPage(position: Int): OnboardingPageFragment?
}

class OnboardingPageManagerWithTrackerBlocking(
    private val defaultRoleBrowserDialog: DefaultRoleBrowserDialog,
    private val onboardingPageBuilder: OnboardingPageBuilder,
    private val defaultWebBrowserCapability: DefaultBrowserDetector,
) : OnboardingPageManager {

    private val pages = mutableListOf<OnboardingPageBlueprint>()

    override fun pageCount() = pages.size

    override fun buildPageBlueprints() {
        pages.clear()

        pages.add(ExperimentWelcomeBluePrint)

        if (shouldShowDefaultBrowserPage()) {
            pages.add((DefaultBrowserBlueprint))
        }
    }

    override fun buildPage(position: Int): OnboardingPageFragment? {
        return when (pages.getOrNull(position)) {
            is ExperimentWelcomeBluePrint -> buildExperimentWelcomePage()
            is DefaultBrowserBlueprint -> buildDefaultBrowserPage()
            else -> null
        }
    }

    private fun shouldShowDefaultBrowserPage(): Boolean {
        return defaultWebBrowserCapability.deviceSupportsDefaultBrowserConfiguration() &&
            !defaultWebBrowserCapability.isDefaultBrowser() &&
            !defaultRoleBrowserDialog.shouldShowDialog()
    }

    private fun buildDefaultBrowserPage(): DefaultBrowserPage {
        return onboardingPageBuilder.buildDefaultBrowserPage()
    }

    private fun buildExperimentWelcomePage(): WelcomePage {
        return onboardingPageBuilder.buildExperimentWelcomePage()
    }
}
