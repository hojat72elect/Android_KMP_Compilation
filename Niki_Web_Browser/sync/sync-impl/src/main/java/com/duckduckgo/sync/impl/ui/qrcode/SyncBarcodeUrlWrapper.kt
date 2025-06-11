

package com.duckduckgo.sync.impl.ui.qrcode

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.impl.SyncDeviceIds
import com.duckduckgo.sync.impl.applyUrlSafetyFromB64
import com.squareup.anvil.annotations.ContributesBinding
import java.net.URLEncoder
import javax.inject.Inject
import timber.log.Timber

interface SyncBarcodeUrlWrapper {

    /**
     * Will accept a sync code and format it so it's wrapped in a URL.
     *
     * @param originalCodeB64Encoded the original base64-encoded code to be modified.
     */
    fun wrapCodeInUrl(originalCodeB64Encoded: String): String
}

@ContributesBinding(AppScope::class)
class SyncBarcodeUrlUrlWrapper @Inject constructor(
    private val syncDeviceIds: SyncDeviceIds,
) : SyncBarcodeUrlWrapper {

    override fun wrapCodeInUrl(originalCodeB64Encoded: String): String {
        return originalCodeB64Encoded.wrapInUrl().also {
            Timber.v("Sync: code to include in the barcode is $it")
        }
    }

    private fun String.wrapInUrl(): String {
        return kotlin.runCatching {
            val urlSafeCode = this.applyUrlSafetyFromB64()
            SyncBarcodeUrl(webSafeB64EncodedCode = urlSafeCode, urlEncodedDeviceName = getDeviceName()).asUrl()
        }.getOrElse {
            Timber.w("Sync-url: Failed to encode string for use inside a URL; returning original code")
            this
        }
    }

    private fun getDeviceName(): String {
        val deviceName = syncDeviceIds.deviceName()
        return URLEncoder.encode(deviceName, "UTF-8")
    }
}
