

package com.duckduckgo.autofill.impl.deviceauth

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import timber.log.Timber

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class AutofillGracePeriodLifecycleObserver @Inject constructor(
    private val gracePeriod: AutofillAuthorizationGracePeriod,
) : MainProcessLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.d("App in background, invalidating autofill grace period")
        gracePeriod.invalidate()
    }
}
