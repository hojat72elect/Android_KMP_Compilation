package ca.hojat.gamehub.core.data.api.igdb.games

import ca.hojat.gamehub.core.data.api.common.ApiResult
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetComingSoonGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetMostAnticipatedGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetPopularGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetRecentlyReleasedGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.SearchGamesRequest
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton

interface GamesEndpoint {
    suspend fun searchGames(request: SearchGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getPopularGames(request: GetPopularGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getRecentlyReleasedGames(request: GetRecentlyReleasedGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getComingSoonGames(request: GetComingSoonGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getMostAnticipatedGames(request: GetMostAnticipatedGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getGames(request: GetGamesRequest): ApiResult<List<ApiGame>>
}

/**
 * This Singleton object will provide all the IGDB related API responses.
 */
@Singleton
@BindType
class GamesEndpointImpl @Inject constructor(
    private val gamesService: GamesService,
    private val igdbApiQueryFactory: IgdbApiQueryFactory,
) : GamesEndpoint {

    override suspend fun searchGames(request: SearchGamesRequest): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createGamesSearchingQuery(request),
        )
    }

    override suspend fun getPopularGames(request: GetPopularGamesRequest): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createPopularGamesRetrievalQuery(request),
        )
    }

    override suspend fun getRecentlyReleasedGames(
        request: GetRecentlyReleasedGamesRequest
    ): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createRecentlyReleasedGamesRetrievalQuery(request),
        )
    }

    override suspend fun getComingSoonGames(request: GetComingSoonGamesRequest): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createComingSoonGamesRetrievalQuery(request),
        )
    }

    override suspend fun getMostAnticipatedGames(
        request: GetMostAnticipatedGamesRequest
    ): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createMostAnticipatedGamesRetrievalQuery(request),
        )
    }

    override suspend fun getGames(request: GetGamesRequest): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createGamesRetrievalQuery(request),
        )
    }
}
