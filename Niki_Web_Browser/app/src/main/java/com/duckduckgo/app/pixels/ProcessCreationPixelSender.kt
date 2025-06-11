

package com.duckduckgo.app.pixels

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.lifecycle.VpnProcessLifecycleObserver
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class ProcessCreationMainPixelSender @Inject constructor(
    private val pixel: Pixel,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        pixel.fire(AppPixelName.PROCESS_CREATED_MAIN)
    }
}

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = VpnProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class ProcessCreationVpnPixelSender @Inject constructor(
    private val pixel: Pixel,
) : VpnProcessLifecycleObserver {

    override fun onVpnProcessCreated() {
        pixel.fire(AppPixelName.PROCESS_CREATED_VPN)
    }
}
