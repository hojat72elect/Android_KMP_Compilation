

package com.duckduckgo.espresso

import android.view.*
import androidx.test.espresso.*
import androidx.test.espresso.matcher.*
import org.hamcrest.*

// used to introduce a delay not blocking main thread
fun waitFor(delay: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
        override fun getDescription(): String = "wait for $delay milliseconds"
        override fun perform(uiController: UiController, v: View?) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }
}
