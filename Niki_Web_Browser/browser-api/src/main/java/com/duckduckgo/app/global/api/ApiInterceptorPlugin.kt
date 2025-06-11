

package com.duckduckgo.app.global.api

import okhttp3.Interceptor

interface ApiInterceptorPlugin {
    fun getInterceptor(): Interceptor
}
