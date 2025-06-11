package ca.hojat.gamehub.core.data.games.datastores

import ca.hojat.gamehub.core.data.database.games.entities.DbGame
import ca.hojat.gamehub.core.data.games.common.DiscoveryGamesReleaseDatesProvider
import ca.hojat.gamehub.core.data.database.games.tables.GamesTable
import ca.hojat.gamehub.core.domain.common.DispatcherProvider
import ca.hojat.gamehub.core.domain.entities.Pagination
import ca.hojat.gamehub.core.domain.games.repository.GamesLocalDataSource
import ca.hojat.gamehub.core.domain.entities.Company
import ca.hojat.gamehub.core.domain.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
class GamesDatabaseDataSource @Inject constructor(
    private val gamesTable: GamesTable,
    private val dispatcherProvider: DispatcherProvider,
    private val discoveryGamesReleaseDatesProvider: DiscoveryGamesReleaseDatesProvider,
    private val dbGameMapper: DbGameMapper,
) : GamesLocalDataSource {

    override suspend fun saveGames(games: List<Game>) {
        gamesTable.saveGames(
            withContext(dispatcherProvider.computation) {
                dbGameMapper.mapToDatabaseGames(games)
            }
        )
    }

    override suspend fun getGame(id: Int): Game? {
        return gamesTable.getGame(id)
            ?.let { databaseGame ->
                withContext(dispatcherProvider.computation) {
                    dbGameMapper.mapToDomainGame(databaseGame)
                }
            }
    }

    override suspend fun getCompanyDevelopedGames(
        company: Company,
        pagination: Pagination
    ): List<Game> {
        return gamesTable.getGames(
            ids = company.developedGames,
            offset = pagination.offset,
            limit = pagination.limit
        )
            .toDataGames()
    }

    override suspend fun getSimilarGames(game: Game, pagination: Pagination): List<Game> {
        return gamesTable.getGames(
            ids = game.similarGames,
            offset = pagination.offset,
            limit = pagination.limit
        )
            .toDataGames()
    }

    override suspend fun searchGames(searchQuery: String, pagination: Pagination): List<Game> {
        return gamesTable.searchGames(
            searchQuery = searchQuery,
            offset = pagination.offset,
            limit = pagination.limit
        )
            .let { databaseGames ->
                withContext(dispatcherProvider.computation) {
                    dbGameMapper.mapToDomainGames(databaseGames)
                }
            }
    }

    override fun observePopularGames(pagination: Pagination): Flow<List<Game>> {
        return gamesTable.observePopularGames(
            minReleaseDateTimestamp = discoveryGamesReleaseDatesProvider.getPopularGamesMinReleaseDate(),
            offset = pagination.offset,
            limit = pagination.limit
        )
            .toDataGamesFlow()
    }

    override fun observeRecentlyReleasedGames(pagination: Pagination): Flow<List<Game>> {
        return gamesTable.observeRecentlyReleasedGames(
            minReleaseDateTimestamp = discoveryGamesReleaseDatesProvider.getRecentlyReleasedGamesMinReleaseDate(),
            maxReleaseDateTimestamp = discoveryGamesReleaseDatesProvider.getRecentlyReleasedGamesMaxReleaseDate(),
            offset = pagination.offset,
            limit = pagination.limit
        )
            .toDataGamesFlow()
    }

    override fun observeComingSoonGames(pagination: Pagination): Flow<List<Game>> {
        return gamesTable.observeComingSoonGames(
            minReleaseDateTimestamp = discoveryGamesReleaseDatesProvider.getComingSoonGamesMinReleaseDate(),
            offset = pagination.offset,
            limit = pagination.limit
        )
            .toDataGamesFlow()
    }

    override fun observeMostAnticipatedGames(pagination: Pagination): Flow<List<Game>> {
        return gamesTable.observeMostAnticipatedGames(
            minReleaseDateTimestamp = discoveryGamesReleaseDatesProvider.getMostAnticipatedGamesMinReleaseDate(),
            offset = pagination.offset,
            limit = pagination.limit
        )
            .toDataGamesFlow()
    }

    private suspend fun List<DbGame>.toDataGames(): List<Game> {
        return withContext(dispatcherProvider.computation) {
            dbGameMapper.mapToDomainGames(this@toDataGames)
        }
    }

    private fun Flow<List<DbGame>>.toDataGamesFlow(): Flow<List<Game>> {
        return distinctUntilChanged()
            .map(dbGameMapper::mapToDomainGames)
            .flowOn(dispatcherProvider.computation)
    }
}
