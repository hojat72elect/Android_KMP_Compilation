package ca.hojat.gamehub.core.data.api.igdb.games

import ca.hojat.gamehub.core.data.api.common.ApiResult
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame
import retrofit2.http.Body
import retrofit2.http.POST

interface GamesService {

    @POST("games")
    suspend fun getGames(@Body body: String): ApiResult<List<ApiGame>>
}
