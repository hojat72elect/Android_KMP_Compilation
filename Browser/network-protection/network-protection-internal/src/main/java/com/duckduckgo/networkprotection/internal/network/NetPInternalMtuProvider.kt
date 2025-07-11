

package com.duckduckgo.networkprotection.internal.network

import android.content.SharedPreferences
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import javax.inject.Inject

class NetPInternalMtuProvider @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) {
    private val preferences: SharedPreferences by lazy {
        sharedPreferencesProvider.getSharedPreferences(
            FILENAME,
            multiprocess = true,
            migrate = false,
        )
    }

    internal fun getMtu(): Int {
        return preferences.getInt(MTU_SIZE, 1280)
    }

    internal fun setMtu(mtu: Int?) {
        if (mtu == null) {
            preferences.edit().remove(MTU_SIZE).apply()
        } else {
            preferences.edit().putInt(MTU_SIZE, mtu).apply()
        }
    }
}

private const val FILENAME = "com.duckduckgo.netp.internal.mtu.v1"
private const val MTU_SIZE = "MTU_SIZE"
