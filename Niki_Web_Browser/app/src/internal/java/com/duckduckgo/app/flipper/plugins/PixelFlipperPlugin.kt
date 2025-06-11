package com.duckduckgo.app.flipper.plugins

import com.duckduckgo.common.utils.plugins.pixel.PixelInterceptorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.facebook.flipper.core.FlipperConnection
import com.facebook.flipper.core.FlipperObject
import com.facebook.flipper.core.FlipperPlugin
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.SingleInstanceIn
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

@SingleInstanceIn(AppScope::class)
class PixelFlipperPlugin @Inject constructor() :
    FlipperPlugin,
    Interceptor,
    PixelInterceptorPlugin {

    private var connection: FlipperConnection? = null

    override fun getId(): String {
        return "ddg-pixels"
    }

    override fun onConnect(connection: FlipperConnection?) {
        Timber.v("$id: connected")
        this.connection = connection
    }

    override fun onDisconnect() {
        Timber.v("$id: disconnected")
        connection = null
    }

    override fun runInBackground(): Boolean {
        Timber.v("$id: running")
        return false
    }

    private fun newRow(row: FlipperObject) {
        connection?.send("newData", row)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (connection == null) {
            return chain.proceed(chain.request())
        }

        val request = chain.request()

        val pixelPayload = mutableMapOf<String, List<String?>>()
        request.url.queryParameterNames.forEach { param ->
            pixelPayload[param] = request.url.queryParameterValues(param)
        }
        FlipperObject.Builder()
            .put("time", LocalTime.now())
            .put("pixel", request.url.pathSegments.last())
            .put("payload", pixelPayload)
            .build()
            .also { newRow(it) }

        return chain.proceed(request)
    }

    override fun getInterceptor(): Interceptor {
        return this
    }
}

@Module
@ContributesTo(AppScope::class)
abstract class PixelFlipperPluginModule {
    @Binds
    @IntoSet
    abstract fun bindPixelFlipperPlugin(pixelFlipperPlugin: PixelFlipperPlugin): FlipperPlugin

    @Binds
    @IntoSet
    abstract fun bindPixelFlipperPluginInterceptor(pixelFlipperPlugin: PixelFlipperPlugin): PixelInterceptorPlugin
}
