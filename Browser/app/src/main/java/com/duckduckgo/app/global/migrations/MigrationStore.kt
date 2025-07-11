

package com.duckduckgo.app.global.migrations

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

interface MigrationStore {
    var version: Int
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class MigrationSharedPreferences @Inject constructor(private val context: Context) : MigrationStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }

    override var version: Int
        get() = preferences.getInt(KEY_VERSION, 0)
        set(version) = preferences.edit { putInt(KEY_VERSION, version) }

    companion object {
        const val FILENAME = "com.duckduckgo.app.global.migrations"
        const val KEY_VERSION = "KEY_VERSION"
    }
}
