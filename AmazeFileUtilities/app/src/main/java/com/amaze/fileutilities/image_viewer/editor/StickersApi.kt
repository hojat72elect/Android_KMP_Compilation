package com.amaze.fileutilities.image_viewer.editor

import com.amaze.fileutilities.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface StickersApi {

    companion object {
        const val API_STICKERS_BASE = BuildConfig.BASE_API_STICKER_PACK
        const val API_QUERY_PARAM = "token"
        const val API_QUERY_PARAM_VALUE = BuildConfig.API_REQ_STICKER_PACK_TOKEN
    }

    @Headers(value = ["Accept: application/json"])
    @GET(API_STICKERS_BASE)
    fun getStickerList(
        @Query(value = API_QUERY_PARAM) query: String = API_QUERY_PARAM_VALUE,
    ): Call<ArrayList<String>>?
}
