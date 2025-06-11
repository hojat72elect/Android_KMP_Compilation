

package com.duckduckgo.sync.impl.di

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.engine.SyncableDataProvider

/**
 * This is here to trigger the code generations
 * [SyncablePlugin] lives in the api module but we don't want to add
 * anvil dependencies
 * CodeGen should not be generated in public api modules.
 */
@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = SyncableDataProvider::class,
)
@Suppress("unused")
private interface UnusedSyncableDataProviderPluginCodegenTrigger
