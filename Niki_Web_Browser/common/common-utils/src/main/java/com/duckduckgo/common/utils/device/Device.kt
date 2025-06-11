

package com.duckduckgo.common.utils.device

import android.content.Context
import android.telephony.TelephonyManager
import android.util.TypedValue
import com.duckduckgo.common.utils.device.DeviceInfo.FormFactor
import com.duckduckgo.common.utils.device.DeviceInfo.FormFactor.PHONE
import com.duckduckgo.common.utils.device.DeviceInfo.FormFactor.TABLET
import java.util.*

interface DeviceInfo {

    enum class FormFactor(val description: String) {
        PHONE("phone"),
        TABLET("tablet"),
    }

    val appVersion: String

    val majorAppVersion: String

    val language: String

    val country: String

    fun formFactor(): FormFactor
}

class ContextDeviceInfo(private val context: Context) : DeviceInfo {

    override val appVersion by lazy {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        info.versionName.orEmpty()
    }

    override val majorAppVersion by lazy { appVersion.split(".").first() }

    override val language: String by lazy { Locale.getDefault().language }

    override val country: String by lazy {
        val telephonyCountry = telephonyManager.networkCountryIso
        val deviceCountry =
            if (telephonyCountry.isNotBlank()) telephonyCountry else Locale.getDefault().country
        deviceCountry.lowercase(Locale.getDefault())
    }

    private val telephonyManager by lazy {
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun formFactor(): FormFactor {
        val metrics = context.resources.displayMetrics
        val smallestSize = Math.min(metrics.widthPixels, metrics.heightPixels)
        val tabletSize =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                600f,
                context.resources.displayMetrics,
            )
                .toInt()
        return if (smallestSize >= tabletSize) {
            TABLET
        } else {
            PHONE
        }
    }
}
