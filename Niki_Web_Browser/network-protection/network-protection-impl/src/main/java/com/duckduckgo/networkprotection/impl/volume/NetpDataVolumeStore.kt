

package com.duckduckgo.networkprotection.impl.volume

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.impl.volume.NetpDataVolumeStore.DataVolume
import com.duckduckgo.networkprotection.store.NetworkProtectionPrefs
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface NetpDataVolumeStore {
    var dataVolume: DataVolume
    data class DataVolume(
        val receivedBytes: Long = 0L,
        val transmittedBytes: Long = 0L,
    )
}

@ContributesBinding(AppScope::class)
class RealNetpDataVolumeStore @Inject constructor(
    private val networkProtectionPrefs: NetworkProtectionPrefs,
) : NetpDataVolumeStore {
    override var dataVolume: DataVolume
        get() {
            return kotlin.runCatching {
                DataVolume(
                    receivedBytes = networkProtectionPrefs.getLong(KEY_RECEIVED_BYTES, 0L),
                    transmittedBytes = networkProtectionPrefs.getLong(KEY_TRANSMITTED_BYTES, 0L),
                )
            }.getOrDefault(DataVolume())
        }
        set(value) {
            kotlin.runCatching {
                networkProtectionPrefs.putLong(KEY_RECEIVED_BYTES, value.receivedBytes)
                networkProtectionPrefs.putLong(KEY_TRANSMITTED_BYTES, value.transmittedBytes)
            }
        }

    companion object {
        private const val KEY_RECEIVED_BYTES = "key_received_bytes"
        private const val KEY_TRANSMITTED_BYTES = "key_transmitted_bytes"
    }
}
