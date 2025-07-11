

package com.duckduckgo.newtabpage.impl.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.SingleInstanceIn
import javax.inject.Inject

interface NewTabSettingsStore {
    var sectionSettings: List<String>
    var shortcutSettings: List<String>
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class AppNewTabSettingsStore @Inject constructor(private val context: Context) : NewTabSettingsStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }
    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter: JsonAdapter<List<String>> = Moshi.Builder().build().adapter(stringListType)

    override var sectionSettings: List<String>
        get() = toStringList(preferences.getString(KEY_NEW_TAB_SECTION_SETTINGS_ORDER, null))
        set(value) = preferences.edit(true) { putString(KEY_NEW_TAB_SECTION_SETTINGS_ORDER, fromStringList(value)) }

    override var shortcutSettings: List<String>
        get() = toStringList(preferences.getString(KEY_NEW_TAB_SHORTCUT_SETTINGS_ORDER, null))
        set(value) = preferences.edit(true) { putString(KEY_NEW_TAB_SHORTCUT_SETTINGS_ORDER, fromStringList(value)) }

    private fun toStringList(value: String?): List<String> {
        if (value != null) {
            return stringListAdapter.fromJson(value)!!
        } else {
            return emptyList()
        }
    }

    private fun fromStringList(value: List<String>): String {
        return stringListAdapter.toJson(value)
    }

    companion object {
        const val FILENAME = "com.duckduckgo.newtabpage.settings"
        private const val KEY_NEW_TAB_SECTION_SETTINGS_ORDER = "KEY_NEW_TAB_SECTION_SETTINGS_ORDER"
        private const val KEY_NEW_TAB_SHORTCUT_SETTINGS_ORDER = "KEY_NEW_TAB_SHORTCUT_SETTINGS_ORDER"
    }
}
