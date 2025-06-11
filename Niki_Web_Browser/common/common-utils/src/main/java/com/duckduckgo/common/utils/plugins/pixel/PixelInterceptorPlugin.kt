

package com.duckduckgo.common.utils.plugins.pixel

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import okhttp3.Interceptor

@ContributesPluginPoint(AppScope::class)
interface PixelInterceptorPlugin {
    fun getInterceptor(): Interceptor
}
