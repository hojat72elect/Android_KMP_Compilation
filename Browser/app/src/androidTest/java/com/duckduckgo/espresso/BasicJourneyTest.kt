

package com.duckduckgo.espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duckduckgo.app.browser.BrowserActivity
import com.duckduckgo.app.browser.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BasicJourneyTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule
    var activityScenarioRule = activityScenarioRule<BrowserActivity>()

    @Test @UserJourney
    fun browser_openPopUp() {
        // since we use a fake toolbar, we want to wait until the real one is visible
        onView(isRoot()).perform(waitForView(withId(R.id.browserMenu)))

        // tap on PopupMenu
        onView(withId(R.id.browserMenu)).perform(click())

        // check that the forward arrow is visible
        onView(withId(R.id.forwardMenuItem)).check(matches(isDisplayed()))
    }
}
