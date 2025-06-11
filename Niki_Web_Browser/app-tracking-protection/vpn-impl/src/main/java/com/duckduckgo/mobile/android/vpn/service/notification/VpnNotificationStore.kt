

package com.duckduckgo.mobile.android.vpn.service.notification

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VpnNotificationStore {
    var persistentNotifDimissedTimestamp: Long
}

@ContributesBinding(AppScope::class)
class RealVpnNotificationStore @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : VpnNotificationStore {
    private val prefs: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(FILENAME, multiprocess = true, migrate = false)
    }

    override var persistentNotifDimissedTimestamp: Long
        get() = prefs.getLong(KEY_PERSISTENT_NOTIF_DISMISSED_TIMESTAMP, 0L)
        set(value) {
            prefs.edit { putLong(KEY_PERSISTENT_NOTIF_DISMISSED_TIMESTAMP, value) }
        }

    companion object {
        private const val FILENAME = "com.duckduckgo.mobile.android.vpn.service.notification.v1"
        private const val KEY_PERSISTENT_NOTIF_DISMISSED_TIMESTAMP = "key_persistent_notif_dismissed_timestamp"
    }
}
