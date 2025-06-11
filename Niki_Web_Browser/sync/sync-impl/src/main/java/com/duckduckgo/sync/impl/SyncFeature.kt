

package com.duckduckgo.sync.impl

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "sync",
)
interface SyncFeature {
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @Toggle.InternalAlwaysEnabled
    fun self(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun level0ShowSync(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun level1AllowDataSyncing(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun level2AllowSetupFlows(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun level3AllowCreateAccount(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun gzipPatchRequests(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun seamlessAccountSwitching(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun exchangeKeysToSyncWithAnotherDevice(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun automaticallyUpdateSyncSettings(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun syncSetupBarcodeIsUrlBased(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun canScanUrlBasedSyncSetupBarcodes(): Toggle
}
