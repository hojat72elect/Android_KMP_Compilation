

package com.duckduckgo.autofill.sync

import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.sync.api.SyncMessagePlugin
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

/**
 * This module provides the [SyncMessagePlugin]s that are used by the Sync feature.
 * Anvil produces a runtime error, so we have to use a workaround to make it work.
 */
@Module
@ContributesTo(ActivityScope::class)
abstract class SyncMessagePluginModule {
    @Binds @IntoSet
    abstract fun providesCredentialsSyncPausedSyncMessagePlugin(messagePlugin: CredentialsSyncPausedSyncMessagePlugin): SyncMessagePlugin
}
