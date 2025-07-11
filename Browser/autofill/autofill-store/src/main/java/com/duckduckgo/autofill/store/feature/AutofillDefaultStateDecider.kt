

package com.duckduckgo.autofill.store.feature

import com.duckduckgo.autofill.api.AutofillFeature
import com.duckduckgo.autofill.api.InternalTestUserChecker
import com.duckduckgo.browser.api.UserBrowserProperties
import timber.log.Timber

interface AutofillDefaultStateDecider {
    fun defaultState(): Boolean
}

class RealAutofillDefaultStateDecider(
    private val userBrowserProperties: UserBrowserProperties,
    private val autofillFeature: AutofillFeature,
    private val internalTestUserChecker: InternalTestUserChecker,
) : AutofillDefaultStateDecider {

    override fun defaultState(): Boolean {
        if (internalTestUserChecker.isInternalTestUser) {
            Timber.v("Internal testing user, enabling autofill by default")
            return true
        }

        if (!autofillFeature.onByDefault().isEnabled()) {
            return false
        }

        if (userBrowserProperties.daysSinceInstalled() > 0L && !autofillFeature.onForExistingUsers().isEnabled()) {
            return false
        }

        Timber.i("Determined Autofill should be enabled by default")
        return true
    }
}
