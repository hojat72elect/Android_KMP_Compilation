

package com.duckduckgo.app.global.install

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.UiThread
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import timber.log.Timber

interface AppInstallStore : MainProcessLifecycleObserver {
    var installTimestamp: Long

    var widgetInstalled: Boolean

    var defaultBrowser: Boolean

    var newDefaultBrowserDialogCount: Int

    fun hasInstallTimestampRecorded(): Boolean
}

fun AppInstallStore.daysInstalled(): Long {
    return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - installTimestamp)
}

class AppInstallSharedPreferences @Inject constructor(private val context: Context) : AppInstallStore {
    override var installTimestamp: Long
        get() = preferences.getLong(KEY_TIMESTAMP_UTC, 0L)
        set(timestamp) = preferences.edit { putLong(KEY_TIMESTAMP_UTC, timestamp) }

    override var widgetInstalled: Boolean
        get() = preferences.getBoolean(KEY_WIDGET_INSTALLED, false)
        set(widgetInstalled) = preferences.edit { putBoolean(KEY_WIDGET_INSTALLED, widgetInstalled) }

    override var defaultBrowser: Boolean
        get() = preferences.getBoolean(KEY_DEFAULT_BROWSER, false)
        set(defaultBrowser) = preferences.edit { putBoolean(KEY_DEFAULT_BROWSER, defaultBrowser) }

    override var newDefaultBrowserDialogCount: Int
        get() = preferences.getInt(ROLE_MANAGER_BROWSER_DIALOG_KEY, 0)
        set(defaultBrowser) = preferences.edit { putInt(ROLE_MANAGER_BROWSER_DIALOG_KEY, defaultBrowser) }

    override fun hasInstallTimestampRecorded(): Boolean = preferences.contains(KEY_TIMESTAMP_UTC)

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    @UiThread
    override fun onCreate(owner: LifecycleOwner) {
        Timber.i("recording installation timestamp")
        if (!hasInstallTimestampRecorded()) {
            installTimestamp = System.currentTimeMillis()
        }
    }

    companion object {
        @VisibleForTesting
        const val FILENAME = "com.duckduckgo.app.install.settings"
        const val KEY_TIMESTAMP_UTC = "INSTALL_TIMESTAMP_UTC"
        const val KEY_WIDGET_INSTALLED = "KEY_WIDGET_INSTALLED"
        const val KEY_DEFAULT_BROWSER = "KEY_DEFAULT_BROWSER"
        private const val ROLE_MANAGER_BROWSER_DIALOG_KEY = "ROLE_MANAGER_BROWSER_DIALOG_KEY"
    }
}
