package com.duckduckgo.autoconsent.impl.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.autoconsent.impl.remoteconfig.AutoconsentFeature
import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface AutoconsentSettingsDataStore {
    var userSetting: Boolean
    var firstPopupHandled: Boolean
    fun invalidateCache()
}

class RealAutoconsentSettingsDataStore constructor(
    private val context: Context,
    private val autoconsentFeature: AutoconsentFeature,
    private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : AutoconsentSettingsDataStore {

    private val preferences: SharedPreferences by lazy { context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE) }
    private var cachedInternalUserSetting: Boolean? = null

    private var _defaultValue: Boolean? = null
    private val defaultValue: Boolean
        get() {
            if (_defaultValue == null) {
                _defaultValue = autoconsentFeature.onByDefault().isEnabled()
            }
            return _defaultValue!!
        }

    init {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            cachedInternalUserSetting = preferences.getBoolean(AUTOCONSENT_USER_SETTING, defaultValue)
        }
    }

    override var userSetting: Boolean
        get() {
            return cachedInternalUserSetting ?: preferences.getBoolean(AUTOCONSENT_USER_SETTING, defaultValue).also {
                cachedInternalUserSetting = it
            }
        }
        set(value) {
            preferences.edit(commit = true) {
                putBoolean(AUTOCONSENT_USER_SETTING, value)
            }.also {
                cachedInternalUserSetting = value
            }
        }

    override var firstPopupHandled: Boolean
        get() = preferences.getBoolean(AUTOCONSENT_FIRST_POPUP_HANDLED, false)
        set(value) {
            preferences.edit(commit = true) {
                putBoolean(AUTOCONSENT_FIRST_POPUP_HANDLED, value)
            }
        }

    override fun invalidateCache() {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            _defaultValue = autoconsentFeature.onByDefault().isEnabled()
            cachedInternalUserSetting = null // invalidate cache
        }
    }

    companion object {
        private const val FILENAME = "com.duckduckgo.autoconsent.store.settings"
        private const val AUTOCONSENT_USER_SETTING = "AutoconsentUserSetting"
        private const val AUTOCONSENT_FIRST_POPUP_HANDLED = "AutoconsentFirstPopupHandled"
    }
}
