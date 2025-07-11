

package com.duckduckgo.app.global.migrations

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.common.utils.plugins.migrations.MigrationPlugin
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
@SingleInstanceIn(AppScope::class)
class MigrationLifecycleObserver @Inject constructor(
    private val migrationPluginPoint: PluginPoint<MigrationPlugin>,
    private val migrationStore: MigrationStore,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        val currentVersion = migrationStore.version
        migrationPluginPoint.getPlugins()
            .sortedBy { it.version }
            .filter {
                currentVersion < it.version
            }.forEach {
                it.run()
            }

        if (currentVersion < CURRENT_VERSION) {
            migrationStore.version = CURRENT_VERSION
        }
    }

    companion object {
        const val CURRENT_VERSION = 2
    }
}
