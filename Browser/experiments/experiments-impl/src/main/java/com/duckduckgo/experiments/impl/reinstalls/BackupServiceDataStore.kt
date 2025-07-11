

package com.duckduckgo.experiments.impl.reinstalls

import android.content.Context
import android.content.SharedPreferences
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface BackupServiceDataStore {
    fun clearBackupPreferences()
}

@ContributesBinding(AppScope::class)
class BackupServiceSharedPreferences @Inject constructor(
    private val context: Context,
) : BackupServiceDataStore {

    private val backupServicePreferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME_BACKUP_SERVICE, Context.MODE_PRIVATE) }
    private val backupPixelPreferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME_BACKUP_PIXEL, Context.MODE_PRIVATE) }

    override fun clearBackupPreferences() {
        if (backupServicePreferences.contains(KEY_ATB)) {
            backupServicePreferences.edit().clear().apply()
        }
        if (backupPixelPreferences.contains(KEY_PIXEL_SENT)) {
            backupPixelPreferences.edit().clear().apply()
        }
    }

    companion object {
        private const val FILENAME_BACKUP_SERVICE = "com.duckduckgo.app.statistics.backup"
        private const val KEY_ATB = "com.duckduckgo.app.statistics.backup.atb"
        private const val FILENAME_BACKUP_PIXEL = "com.duckduckgo.app.statistics.backup.pixel"
        private const val KEY_PIXEL_SENT = "com.duckduckgo.app.statistics.backup.pixel.sent"
    }
}
