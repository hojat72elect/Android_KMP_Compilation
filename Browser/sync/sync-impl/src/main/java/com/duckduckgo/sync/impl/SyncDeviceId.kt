

package com.duckduckgo.sync.impl

import android.annotation.SuppressLint
import android.os.Build
import com.duckduckgo.common.utils.device.DeviceInfo
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.DeviceSyncState.Type
import com.duckduckgo.sync.api.DeviceSyncState.Type.DESKTOP
import com.duckduckgo.sync.api.DeviceSyncState.Type.MOBILE
import com.duckduckgo.sync.api.DeviceSyncState.Type.UNKNOWN
import com.duckduckgo.sync.store.SyncStore
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject

interface SyncDeviceIds {
    fun userId(): String
    fun deviceName(): String
    fun deviceId(): String
    fun deviceType(): DeviceType
}

@ContributesBinding(AppScope::class)
class AppSyncDeviceIds
@Inject
constructor(
    private val syncStore: SyncStore,
    private val deviceInfo: DeviceInfo,
) : SyncDeviceIds {
    override fun userId(): String {
        var userId = syncStore.userId
        if (userId != null) return userId

        userId = UUID.randomUUID().toString()

        return userId
    }

    override fun deviceName(): String {
        var deviceName = syncStore.deviceName
        if (deviceName != null) return deviceName

        deviceName = "${Build.BRAND} ${Build.MODEL}"
        return deviceName
    }

    @SuppressLint("HardwareIds")
    override fun deviceId(): String {
        var deviceId = syncStore.deviceId
        if (deviceId != null) return deviceId

        deviceId = UUID.randomUUID().toString()
        return deviceId
    }

    override fun deviceType(): DeviceType {
        return DeviceType(deviceInfo.formFactor().description)
    }
}

private val mobileRegex = Regex("(phone|tablet)", RegexOption.IGNORE_CASE)
private val desktopRegex = Regex("(desktop)", RegexOption.IGNORE_CASE)

data class DeviceType(val deviceFactor: String = "") {
    fun type(): Type {
        return when {
            deviceFactor.contains(mobileRegex) -> MOBILE
            deviceFactor.contains(desktopRegex) -> DESKTOP
            deviceFactor.isEmpty() -> UNKNOWN
            else -> UNKNOWN
        }
    }
}

fun Type.asDrawableRes(): Int {
    return when (this) {
        MOBILE -> R.drawable.ic_device_mobile_24
        UNKNOWN -> R.drawable.ic_device_mobile_24
        DESKTOP -> R.drawable.ic_device_desktop_24
    }
}
