package ca.hojat.gamehub.core.data.games.datastores

import ca.hojat.gamehub.core.data.database.games.entities.DbGame
import ca.hojat.gamehub.core.data.database.games.tables.LikedGamesTable
import ca.hojat.gamehub.core.domain.common.DispatcherProvider
import ca.hojat.gamehub.core.domain.entities.Pagination
import ca.hojat.gamehub.core.domain.games.repository.LikedGamesLocalDataSource
import ca.hojat.gamehub.core.domain.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
class LikedGamesDatabaseDataSource @Inject constructor(
    private val likedGamesTable: LikedGamesTable,
    private val likedGameFactory: LikedGameFactory,
    private val dispatcherProvider: DispatcherProvider,
    private val dbGameMapper: DbGameMapper,
) : LikedGamesLocalDataSource {

    override suspend fun likeGame(gameId: Int) {
        likedGamesTable.saveLikedGame(likedGameFactory.createLikedGame(gameId))
    }

    override suspend fun unlikeGame(gameId: Int) {
        likedGamesTable.deleteLikedGame(gameId)
    }

    override suspend fun isGameLiked(gameId: Int): Boolean {
        return likedGamesTable.isGameLiked(gameId)
    }

    override fun observeGameLikeState(gameId: Int): Flow<Boolean> {
        return likedGamesTable.observeGameLikeState(gameId)
    }

    override fun observeLikedGames(pagination: Pagination): Flow<List<Game>> {
        return likedGamesTable.observeLikedGames(
            offset = pagination.offset,
            limit = pagination.limit
        )
            .toDataGamesFlow()
    }

    private fun Flow<List<DbGame>>.toDataGamesFlow(): Flow<List<Game>> {
        return distinctUntilChanged()
            .map(dbGameMapper::mapToDomainGames)
            .flowOn(dispatcherProvider.computation)
    }
}
