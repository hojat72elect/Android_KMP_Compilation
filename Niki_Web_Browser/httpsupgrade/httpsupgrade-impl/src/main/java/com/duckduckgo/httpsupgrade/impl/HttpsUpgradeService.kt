

package com.duckduckgo.httpsupgrade.impl

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.httpsupgrade.store.HttpsBloomFilterSpec
import com.duckduckgo.httpsupgrade.store.HttpsFalsePositiveDomain
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

@ContributesServiceApi(AppScope::class)
interface HttpsUpgradeService {

    @GET("https://staticcdn.duckduckgo.com/https/https-mobile-v2-bloom-spec.json")
    fun httpsBloomFilterSpec(): Observable<HttpsBloomFilterSpec>

    @GET("https://staticcdn.duckduckgo.com/https/https-mobile-v2-bloom.bin")
    fun httpsBloomFilter(): Call<ResponseBody>

    @GET("https://staticcdn.duckduckgo.com/https/https-mobile-v2-false-positives.json")
    fun falsePositives(): Call<List<HttpsFalsePositiveDomain>>
}
