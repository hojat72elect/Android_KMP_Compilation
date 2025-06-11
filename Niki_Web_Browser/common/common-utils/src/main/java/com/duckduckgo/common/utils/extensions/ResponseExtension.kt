

package com.duckduckgo.common.utils.extensions

import java.net.HttpURLConnection.HTTP_NOT_MODIFIED
import retrofit2.Response

val <T> Response<T>.isCached: Boolean
    get() = raw().cacheResponse != null && (raw().networkResponse?.code == null || raw().networkResponse?.code == HTTP_NOT_MODIFIED)
