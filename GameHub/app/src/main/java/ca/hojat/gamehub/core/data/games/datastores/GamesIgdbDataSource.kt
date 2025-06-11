package ca.hojat.gamehub.core.data.games.datastores

import ca.hojat.gamehub.core.domain.DomainResult
import ca.hojat.gamehub.core.domain.common.DispatcherProvider
import ca.hojat.gamehub.core.domain.entities.Company
import ca.hojat.gamehub.core.domain.entities.Game
import ca.hojat.gamehub.core.domain.entities.Pagination
import ca.hojat.gamehub.core.domain.games.repository.GamesRemoteDataSource
import ca.hojat.gamehub.core.data.api.ApiErrorMapper
import ca.hojat.gamehub.core.data.api.common.ApiResult
import ca.hojat.gamehub.core.data.api.igdb.games.GamesEndpoint
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetComingSoonGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetMostAnticipatedGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetPopularGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetRecentlyReleasedGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.SearchGamesRequest
import ca.hojat.gamehub.core.data.games.common.DiscoveryGamesReleaseDatesProvider
import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
class GamesIgdbDataSource @Inject constructor(
    private val gamesEndpoint: GamesEndpoint,
    private val releaseDatesProvider: DiscoveryGamesReleaseDatesProvider,
    private val dispatcherProvider: DispatcherProvider,
    private val igdbGameMapper: IgdbGameMapper,
    private val apiErrorMapper: ApiErrorMapper,
) : GamesRemoteDataSource {

    override suspend fun searchGames(
        searchQuery: String,
        pagination: Pagination
    ): DomainResult<List<Game>> {
        return gamesEndpoint
            .searchGames(
                SearchGamesRequest(
                    searchQuery = searchQuery,
                    offset = pagination.offset,
                    limit = pagination.limit,
                )
            )
            .toDataStoreResult()
    }

    override suspend fun getPopularGames(pagination: Pagination): DomainResult<List<Game>> {
        return gamesEndpoint
            .getPopularGames(
                GetPopularGamesRequest(
                    minReleaseDateTimestamp = releaseDatesProvider.getPopularGamesMinReleaseDate(),
                    offset = pagination.offset,
                    limit = pagination.limit,
                )
            )
            .toDataStoreResult()
    }

    override suspend fun getRecentlyReleasedGames(pagination: Pagination): DomainResult<List<Game>> {
        return gamesEndpoint
            .getRecentlyReleasedGames(
                GetRecentlyReleasedGamesRequest(
                    minReleaseDateTimestamp = releaseDatesProvider.getRecentlyReleasedGamesMinReleaseDate(),
                    maxReleaseDateTimestamp = releaseDatesProvider.getRecentlyReleasedGamesMaxReleaseDate(),
                    offset = pagination.offset,
                    limit = pagination.limit,
                )
            )
            .toDataStoreResult()
    }

    override suspend fun getComingSoonGames(pagination: Pagination): DomainResult<List<Game>> {
        return gamesEndpoint
            .getComingSoonGames(
                GetComingSoonGamesRequest(
                    minReleaseDateTimestamp = releaseDatesProvider.getComingSoonGamesMinReleaseDate(),
                    offset = pagination.offset,
                    limit = pagination.limit,
                )
            )
            .toDataStoreResult()
    }

    override suspend fun getMostAnticipatedGames(pagination: Pagination): DomainResult<List<Game>> {
        return gamesEndpoint
            .getMostAnticipatedGames(
                GetMostAnticipatedGamesRequest(
                    minReleaseDateTimestamp = releaseDatesProvider.getMostAnticipatedGamesMinReleaseDate(),
                    offset = pagination.offset,
                    limit = pagination.limit,
                )
            )
            .toDataStoreResult()
    }

    override suspend fun getCompanyDevelopedGames(
        company: Company,
        pagination: Pagination
    ): DomainResult<List<Game>> {
        return gamesEndpoint.getGames(
            GetGamesRequest(
                gameIds = company.developedGames,
                offset = pagination.offset,
                limit = pagination.limit,
            )
        )
            .toDataStoreResult()
    }

    override suspend fun getSimilarGames(
        game: Game,
        pagination: Pagination
    ): DomainResult<List<Game>> {
        return gamesEndpoint
            .getGames(
                GetGamesRequest(
                    gameIds = game.similarGames,
                    offset = pagination.offset,
                    limit = pagination.limit,
                )
            )
            .toDataStoreResult()
    }

    private suspend fun ApiResult<List<ApiGame>>.toDataStoreResult(): DomainResult<List<Game>> {
        return withContext(dispatcherProvider.computation) {
            mapEither(igdbGameMapper::mapToDomainGames, apiErrorMapper::mapToDomainError)
        }
    }
}
