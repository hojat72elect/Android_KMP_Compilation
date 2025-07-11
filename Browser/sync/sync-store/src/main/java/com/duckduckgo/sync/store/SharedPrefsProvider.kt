

package com.duckduckgo.sync.store

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

interface SharedPrefsProvider {
    fun getEncryptedSharedPrefs(fileName: String): SharedPreferences?
    fun getSharedPrefs(fileName: String): SharedPreferences
}

class SyncSharedPrefsProvider(
    private val context: Context,
) : SharedPrefsProvider {
    override fun getEncryptedSharedPrefs(fileName: String): SharedPreferences? {
        return try {
            EncryptedSharedPreferences.create(
                context,
                fileName,
                MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
        } catch (e: Throwable) {
            null
        }
    }

    override fun getSharedPrefs(fileName: String): SharedPreferences {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }
}
