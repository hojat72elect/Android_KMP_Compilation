

package com.duckduckgo.sync.settings.api

/**
 * Interface to notify when a setting was changed by the user.
 */
interface SyncSettingsListener {

    /**
     * Should be called when a setting was changed by the user.
     * @param settingKey the unique identifier of the setting that was changed.
     * It should be the same as @see SyncableSetting.key
     */
    fun onSettingChanged(settingKey: String)
}
