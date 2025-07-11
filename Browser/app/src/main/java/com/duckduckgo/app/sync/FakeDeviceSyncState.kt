

package com.duckduckgo.app.sync

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.DeviceSyncState
import com.duckduckgo.sync.api.DeviceSyncState.SyncAccountState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.*

/**
 * We need to provide this fake implementation for non internal builds until we can add sync modules dependencies for all flavors.
 */
@ContributesBinding(
    scope = AppScope::class,
    rank = ContributesBinding.RANK_NORMAL,
)
class FakeDeviceSyncState @Inject constructor() : DeviceSyncState {
    override fun isFeatureEnabled(): Boolean = false
    override fun isUserSignedInOnDevice(): Boolean = false
    override fun getAccountState(): SyncAccountState = SyncAccountState.SignedOut
}
