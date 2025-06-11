

package com.duckduckgo.espresso.privacy

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.duckduckgo.app.browser.R
import com.duckduckgo.espresso.waitFor
import com.duckduckgo.espresso.waitForView
import java.util.concurrent.TimeUnit

fun preparationsForPrivacyTest() {
    val waitTime = 16000L
    IdlingPolicies.setMasterPolicyTimeout(waitTime * 10, TimeUnit.MILLISECONDS)
    IdlingPolicies.setIdlingResourceTimeout(waitTime * 10, TimeUnit.MILLISECONDS)

    onView(isRoot()).perform(waitForView(withId(R.id.browserMenu)))
    onView(isRoot()).perform(waitFor(2000))

    runCatching {
        onView(withText("Dismiss"))
            .perform(click())
            .check(matches(isDisplayed()))
    }
}
