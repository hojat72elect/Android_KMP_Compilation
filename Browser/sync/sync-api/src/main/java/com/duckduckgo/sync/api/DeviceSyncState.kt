

package com.duckduckgo.sync.api

/** Public interface for Device Sync State */
interface DeviceSyncState {
    /**
     * Checks if feature should be visible to users.
     */
    fun isFeatureEnabled(): Boolean

    /**
     * Checks if user is signed in on this device.
     */
    fun isUserSignedInOnDevice(): Boolean

    /**
     * Returns the sync account state
     */
    fun getAccountState(): SyncAccountState

    /**
     * Representation of the current sync account state, including whether signed in or not.
     * If signed in, it also includes other data.
     */
    sealed interface SyncAccountState {
        data object SignedOut : SyncAccountState
        data class SignedIn(val devices: List<ConnectedDevice>) : SyncAccountState
    }

    /**
     * Representation of a device which is connected through sync.
     */
    data class ConnectedDevice(
        val thisDevice: Boolean,
        val deviceName: String,
        val deviceId: String,
        val deviceType: Type,
    )

    /**
     * The device type for a device connected through sync.
     */
    enum class Type {
        MOBILE,
        UNKNOWN,
        DESKTOP,
        ;
    }
}
