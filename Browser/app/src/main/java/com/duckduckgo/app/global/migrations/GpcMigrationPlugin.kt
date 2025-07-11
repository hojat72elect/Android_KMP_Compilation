

package com.duckduckgo.app.global.migrations

import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.common.utils.plugins.migrations.MigrationPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.Gpc
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import timber.log.Timber

@ContributesMultibinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class GpcMigrationPlugin @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val gpc: Gpc,
) : MigrationPlugin {

    override val version: Int = 1

    override fun run() {
        Timber.d("Migrating gpc settings")
        val gpcEnabled = settingsDataStore.globalPrivacyControlEnabled
        if (gpcEnabled) {
            gpc.enableGpc()
        } else {
            gpc.disableGpc()
        }
    }
}
