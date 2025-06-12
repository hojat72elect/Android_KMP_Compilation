package com.amaze.fileutilities.video_player

import androidx.annotation.Keep
import com.amaze.fileutilities.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface SubtitlesApi {

    companion object {
        const val API_OPEN_SUBTITLES_BASE = "https://api.opensubtitles.com/api/v1/"
        const val API_SEARCH_SUBTITLES = "subtitles"
        const val API_DOWNLOAD_SUBTITLES = "download"
        const val API_LANGUAGE = "infos/languages"
        private const val API_KEY = BuildConfig.OPENSUBTITLES_API_KEY
        private const val USER_AGENT = "AmazeFileUtils"
        val HEADER_API_KEY_MAP = mapOf(
            Pair("Api-Key", API_KEY),
            Pair("User-Agent", USER_AGENT), Pair("Accept", "application/json")
        )
    }

    @GET(API_LANGUAGE)
    fun getLanguageList(@HeaderMap headersMap: Map<String, String>): Call<LanguageResult>?

    @GET(API_SEARCH_SUBTITLES)
    fun getSearchResults(
        @HeaderMap headersMap: Map<String, String>,
        @Query(value = "query") query: String,
        @Query(value = "languages") languages: String
    ): Call<SearchResultsResponse>?

    @POST(API_DOWNLOAD_SUBTITLES)
    fun getDownloadLink(
        @HeaderMap headersMap: Map<String, String>,
        @Body downloadLinkRequest: GetDownloadLinkRequest
    ): Call<GetDownloadLinkResponse>?

    @GET
    @Streaming
    fun downloadSubtitleFile(@Url downloadUrl: String): Call<ResponseBody>?

    @Keep
    data class SearchResultsResponse(val data: List<Data>) {
        @Keep
        data class Attributes(
            val language: String?,
            val ratings: String?,
            val download_count: String?,
            val upload_date: String?,
            val uploader: Uploader?,
            val files: List<Files>?
        )

        @Keep
        data class Uploader(val name: String?, val rank: String?)

        @Keep
        data class Files(val file_id: String?, val file_name: String?, val cd_number: String?)

        @Keep
        data class Data(val attributes: Attributes?)
    }

    @Keep
    data class GetDownloadLinkRequest(val file_id: String?)

    @Keep
    data class GetDownloadLinkResponse(val link: String?, val file_name: String?)

    @Keep
    data class LanguageResult(val data: List<Data>) {
        @Keep
        data class Data(val language_code: String?, val language_name: String?)
    }
}
