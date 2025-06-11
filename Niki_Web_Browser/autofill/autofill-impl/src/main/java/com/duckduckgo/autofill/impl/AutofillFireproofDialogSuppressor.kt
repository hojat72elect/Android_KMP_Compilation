

package com.duckduckgo.autofill.impl

import com.duckduckgo.autofill.impl.time.TimeProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import timber.log.Timber

interface AutofillFireproofDialogSuppressor {
    fun isAutofillPreventingFireproofPrompts(): Boolean
    fun autofillSaveOrUpdateDialogVisibilityChanged(visible: Boolean)
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealAutofillFireproofDialogSuppressor @Inject constructor(private val timeProvider: TimeProvider) : AutofillFireproofDialogSuppressor {

    private var autofillDialogShowing = false
    private var lastTimeUserSawAutofillDialog = 0L

    override fun isAutofillPreventingFireproofPrompts(): Boolean {
        val timeSinceLastDismissed = timeProvider.currentTimeMillis() - lastTimeUserSawAutofillDialog
        val suppressing = autofillDialogShowing || (timeSinceLastDismissed <= TIME_PERIOD_TO_SUPPRESS_FIREPROOF_PROMPT)
        Timber.d(
            "isAutofillPreventingFireproofPrompts: %s (autofillDialogShowing=%s, timeSinceLastDismissed=%sms)",
            suppressing,
            autofillDialogShowing,
            timeSinceLastDismissed,
        )
        return suppressing
    }

    override fun autofillSaveOrUpdateDialogVisibilityChanged(visible: Boolean) {
        Timber.v("Autofill save/update dialog visibility changed to %s", visible)
        autofillDialogShowing = visible

        if (!visible) {
            lastTimeUserSawAutofillDialog = timeProvider.currentTimeMillis()
        }
    }

    companion object {
        private val TIME_PERIOD_TO_SUPPRESS_FIREPROOF_PROMPT = TimeUnit.SECONDS.toMillis(10)
    }
}
