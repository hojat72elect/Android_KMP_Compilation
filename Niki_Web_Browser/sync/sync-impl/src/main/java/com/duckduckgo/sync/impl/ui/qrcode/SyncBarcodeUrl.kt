

package com.duckduckgo.sync.impl.ui.qrcode

import androidx.core.net.toUri

data class SyncBarcodeUrl(
    val webSafeB64EncodedCode: String,
    val urlEncodedDeviceName: String? = null,
) {

    fun asUrl(): String {
        val sb = StringBuilder(URL_BASE)
            .append("&")
            .append(CODE_PARAM).append("=").append(webSafeB64EncodedCode)

        if (urlEncodedDeviceName?.isNotBlank() == true) {
            sb.append("&")
            sb.append(DEVICE_NAME_PARAM).append("=").append(urlEncodedDeviceName)
        }

        return sb.toString()
    }

    companion object {
        const val URL_BASE = "https://duckduckgo.com/sync/pairing/#"
        private const val CODE_PARAM = "code"
        private const val DEVICE_NAME_PARAM = "deviceName"

        fun parseUrl(fullSyncUrl: String): SyncBarcodeUrl? {
            return kotlin.runCatching {
                if (!fullSyncUrl.startsWith(URL_BASE)) {
                    return null
                }

                val uri = fullSyncUrl.toUri()
                val fragment = uri.fragment ?: return null
                val fragmentParts = fragment.split("&")

                val code = fragmentParts
                    .find { it.startsWith("code=") }
                    ?.substringAfter("code=")
                    ?: return null

                val deviceName = fragmentParts
                    .find { it.startsWith("deviceName=") }
                    ?.substringAfter("deviceName=")

                SyncBarcodeUrl(webSafeB64EncodedCode = code, urlEncodedDeviceName = deviceName)
            }.getOrNull()
        }
    }
}
