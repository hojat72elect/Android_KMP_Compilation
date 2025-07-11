

package com.duckduckgo.sync.settings.api

/**
 * A setting that can be synced.
 * Implement this interface to provide a way to sync a setting with the Sync Service.
 */
interface SyncableSetting {
    /**
     * This is the unique identifier of the setting to be used as a key in the Sync Setting collection.
     */
    val key: String

    /**
     * Setting value represented as a string.
     * If the setting is not set or has no value, null should be returned.
     */
    fun getValue(): String?

    /**
     * Should save a new value for the setting.
     * @param value the new value to be stored in the local store. If value not present or deleted, will be null.
     * @return true if local value was changed, false otherwise.
     */
    fun save(value: String?): Boolean

    /**
     * Should provide a way to merge remote value with local value on First sync (Deduplication).
     * @param value the remote value to be merged with the local value. If remote value not present or deleted, will be null.
     * @return true if local value was changed, false otherwise.
     */
    fun deduplicate(value: String?): Boolean

    /**
     * Should provide a way to register to changes applied by Sync Service.
     * When listener notified means that the setting value was changed by the Sync Service.
     */
    fun registerToRemoteChanges(onDataChanged: () -> Unit)

    /**
     * Should provide a way to register to changes applied by the user.
     * When called means that the user changed the setting value, and needs to be synced.
     */
    fun onSettingChanged()

    /**
     * Sync Feature has been disabled / device has been removed
     * This is an opportunity for Features to do some local cleanup if needed
     */
    fun onSyncDisabled()
}
