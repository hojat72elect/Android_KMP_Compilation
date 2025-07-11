

package com.duckduckgo.sync.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.DeviceSyncState
import com.duckduckgo.sync.api.DeviceSyncState.ConnectedDevice
import com.duckduckgo.sync.api.DeviceSyncState.SyncAccountState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.*

@ContributesBinding(
    scope = AppScope::class,
    boundType = DeviceSyncState::class,
    rank = ContributesBinding.RANK_HIGHEST,
)
class AppDeviceSyncState @Inject constructor(
    private val syncFeatureToggle: SyncFeatureToggle,
    private val syncAccountRepository: SyncAccountRepository,
) : DeviceSyncState {

    override fun isUserSignedInOnDevice(): Boolean = syncAccountRepository.isSignedIn()

    override fun getAccountState(): SyncAccountState {
        if (!isUserSignedInOnDevice()) return SyncAccountState.SignedOut
        val devices = syncAccountRepository.getConnectedDevices().getOrNull() ?: emptyList()
        val devicesMapped = devices.map {
            ConnectedDevice(
                thisDevice = it.thisDevice,
                deviceName = it.deviceName,
                deviceId = it.deviceId,
                deviceType = it.deviceType.type(),
            )
        }
        return SyncAccountState.SignedIn(devicesMapped)
    }

    override fun isFeatureEnabled(): Boolean {
        return syncFeatureToggle.showSync()
    }
}
