

package com.duckduckgo.sync.settings.impl

import com.duckduckgo.di.scopes.*
import com.duckduckgo.sync.settings.api.SyncSettingsListener
import com.squareup.anvil.annotations.*
import dagger.*
import javax.inject.Inject
import timber.log.*

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AppSyncSettingsListener @Inject constructor(
    private val syncMetadataDao: SettingsSyncMetadataDao,
) : SyncSettingsListener {
    override fun onSettingChanged(settingKey: String) {
        Timber.i("Sync-Settings: onSettingChanged($settingKey)")
        val entity = SettingsSyncMetadataEntity(
            key = settingKey,
            modified_at = SyncDateProvider.now(),
            deleted_at = null,
        )
        syncMetadataDao.addOrUpdate(entity)
    }
}
