

package com.duckduckgo.app.pixels.campaign.params

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.DeviceSyncState
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class SyncedUsedAdditionalPixelParamPlugin @Inject constructor(
    private val deviceSyncState: DeviceSyncState,
) : AdditionalPixelParamPlugin {
    override suspend fun params(): Pair<String, String> = Pair(
        "syncUsed",
        "${deviceSyncState.isUserSignedInOnDevice()}",
    )
}
