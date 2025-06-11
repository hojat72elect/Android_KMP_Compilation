package ca.hojat.gamehub.core.data.api.gamespot.articles

import ca.hojat.gamehub.core.data.api.common.ApiResult
import ca.hojat.gamehub.core.data.api.gamespot.articles.entities.ApiArticle
import ca.hojat.gamehub.core.data.api.gamespot.common.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ArticlesService {

    @GET("articles")
    suspend fun getArticles(
        @QueryMap queryParams: Map<String, String>
    ): ApiResult<Response<ApiArticle>>
}
