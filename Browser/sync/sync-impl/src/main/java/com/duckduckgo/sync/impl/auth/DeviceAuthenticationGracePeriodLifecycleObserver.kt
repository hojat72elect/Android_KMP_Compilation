

package com.duckduckgo.sync.impl.auth

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
class DeviceAuthenticationGracePeriodLifecycleObserver @Inject constructor(
    private val gracePeriod: DeviceAuthorizationGracePeriod,
) : MainProcessLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.d("App in background, invalidating device auth grace period")
        gracePeriod.invalidate()
    }
}
