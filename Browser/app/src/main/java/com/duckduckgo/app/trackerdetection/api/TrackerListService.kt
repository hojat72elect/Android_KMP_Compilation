

package com.duckduckgo.app.trackerdetection.api

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import retrofit2.Call
import retrofit2.http.GET

@ContributesServiceApi(AppScope::class)
interface TrackerListService {
    @GET("$TDS_BASE_URL$TDS_PATH")
    @TdsRequired
    fun tds(): Call<TdsJson>

    @GET("/contentblocking/trackers-unprotected-temporary.txt")
    fun temporaryAllowList(): Call<String>
}

/**
 * This annotation is used in interceptors to be able to intercept the annotated service calls
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TdsRequired

const val TDS_BASE_URL = "https://staticcdn.duckduckgo.com/trackerblocking/"
const val TDS_PATH = "v5/current/android-tds.json"
