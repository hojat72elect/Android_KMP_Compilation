

package com.duckduckgo.app.feedback.api

import com.duckduckgo.anvil.annotations.ContributesServiceApi
import com.duckduckgo.di.scopes.AppScope
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

@ContributesServiceApi(AppScope::class)
interface FeedbackService {

    @FormUrlEncoded
    @POST("/feedback.js?type=app-feedback")
    suspend fun submitFeedback(
        @Field("reason") reason: String = REASON_GENERAL,
        @Field("rating") rating: String,
        @Field("category") category: String?,
        @Field("subcategory") subcategory: String?,
        @Field("comment") comment: String,
        @Field("url") url: String? = null,
        @Field("platform") platform: String = PLATFORM,
        @Field("v") version: String,
        @Field("os") api: Int,
        @Field("manufacturer") manufacturer: String,
        @Field("model") model: String,
        @Field("atb") atb: String,
    )

    companion object {
        const val REASON_GENERAL = "general"
        private const val PLATFORM = "Android"
    }
}
