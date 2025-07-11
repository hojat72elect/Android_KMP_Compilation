

package com.duckduckgo.espresso

import android.os.Build
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
import androidx.test.filters.SdkSuppress
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.onboarding.ui.OnboardingActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaxDialogsJourneyTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule
    var activityScenarioRule = activityScenarioRule<OnboardingActivity>()

    @Test
    @UserJourney
    @FlakyTest
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.O, maxSdkVersion = Build.VERSION_CODES.P)
    fun daxDialogs_supports_default_browser_journey() {
        onView(isRoot()).perform(waitForView(withId(R.id.primaryCta)))
        onView(withId(R.id.primaryCta)).perform(click())

        onView(isRoot()).perform(waitForView(withId(R.id.continueButton)))
        onView(withId(R.id.continueButton)).perform(click())

        onView(isRoot()).perform(waitForView(withId(R.id.browserMenu)))
        onView(withId(R.id.browserMenu)).perform(click())

        onView(withId(R.id.forwardMenuItem)).check(matches(isDisplayed()))
    }
}
