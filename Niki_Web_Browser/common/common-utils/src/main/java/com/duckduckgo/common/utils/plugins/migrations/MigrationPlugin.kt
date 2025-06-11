

package com.duckduckgo.common.utils.plugins.migrations

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(AppScope::class)
interface MigrationPlugin {
    fun run()
    val version: Int
}
