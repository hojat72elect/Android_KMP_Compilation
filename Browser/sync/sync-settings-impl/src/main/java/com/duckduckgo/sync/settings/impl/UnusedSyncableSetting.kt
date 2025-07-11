

package com.duckduckgo.sync.settings.impl

import com.duckduckgo.anvil.annotations.*
import com.duckduckgo.di.scopes.*
import com.duckduckgo.sync.settings.api.SyncableSetting

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = SyncableSetting::class,
)
@Suppress("unused")
interface UnusedSyncableSetting
