

package com.duckduckgo.app.global.api

import com.duckduckgo.common.utils.device.DeviceInfo
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * This interceptor fixes the re-query pixels without being invasive to the Pixel APIs.
 * When the search box was removed, the rq pixels were moved into the mobile app, however ALL pixels sent
 * from the mobile app have _android_{formFactor} appended to the pixel name, and this broke all existing
 * monitoring and dashboards.
 *
 * This interceptor removes the _android_{formFactor} appended suffix
 */
class PixelReQueryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var url = chain.request().url
        val request = chain.request().newBuilder()

        url = url.toUrl().toString().replace("rq_0_android_${DeviceInfo.FormFactor.PHONE.description}", "rq_0").toHttpUrl()
        url = url.toUrl().toString().replace("rq_0_android_${DeviceInfo.FormFactor.TABLET.description}", "rq_0").toHttpUrl()
        url = url.toUrl().toString().replace("rq_1_android_${DeviceInfo.FormFactor.PHONE.description}", "rq_1").toHttpUrl()
        url = url.toUrl().toString().replace("rq_1_android_${DeviceInfo.FormFactor.TABLET.description}", "rq_1").toHttpUrl()

        Timber.d("Pixel interceptor: $url")

        return chain.proceed(request.url(url).build())
    }
}
