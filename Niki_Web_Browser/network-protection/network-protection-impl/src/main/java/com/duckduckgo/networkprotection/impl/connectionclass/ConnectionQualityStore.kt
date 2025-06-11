

package com.duckduckgo.networkprotection.impl.connectionclass

import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import javax.inject.Inject
import kotlinx.coroutines.withContext

class ConnectionQualityStore @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
    private val dispatcherProvider: DispatcherProvider,
) {

    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(FILENAME, multiprocess = true, migrate = false)
    }
    private var connectionLatencyMs: Int
        get() = preferences.getInt(CONNECTION_QUALITY_TAG, -1)
        set(value) = preferences.edit { putInt(CONNECTION_QUALITY_TAG, value) }

    internal suspend fun saveConnectionLatency(value: Int) = withContext(dispatcherProvider.io()) {
        connectionLatencyMs = value
    }

    suspend fun getConnectionLatency(): Int = withContext(dispatcherProvider.io()) {
        return@withContext connectionLatencyMs
    }

    internal suspend fun reset() = withContext(dispatcherProvider.io()) {
        preferences.edit { clear() }
    }

    companion object {
        private const val FILENAME = "com.duckduckgo.networkprotection.internal.connectionclass.v1"
        private const val CONNECTION_QUALITY_TAG = "CONNECTION_QUALITY_TAG"
    }
}
