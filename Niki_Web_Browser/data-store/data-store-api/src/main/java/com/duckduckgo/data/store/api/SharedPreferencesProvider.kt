

package com.duckduckgo.data.store.api

import android.content.SharedPreferences

interface SharedPreferencesProvider {
    /**
     * Returns an instance of Shared preferences
     * @param name Name of the shared preferences
     * @param multiprocess `true` if the shared preferences will be accessed from several processes else `false`
     * @param migrate `true` if the shared preferences existed prior to use the [SharedPreferencesProvider], else `false`
     */
    fun getSharedPreferences(name: String, multiprocess: Boolean = false, migrate: Boolean = false): SharedPreferences

    /**
     * Returns and instance of Encrypted Shared PReferences
     * @param name Name of the shared preferences
     * @param multiprocess `true` if the shared preferences will be accessed from several processes else `false`
     *
     * @return the encrypted shared preferences of null if there was any error (eg. devices doesn't support it)
     */
    fun getEncryptedSharedPreferences(name: String, multiprocess: Boolean = false): SharedPreferences?
}
