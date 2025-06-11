package ca.hojat.gamehub.core.domain.games.usecases

import ca.hojat.gamehub.core.domain.common.DispatcherProvider
import ca.hojat.gamehub.core.domain.DomainResult
import ca.hojat.gamehub.core.extensions.onEachSuccess
import ca.hojat.gamehub.core.domain.games.RefreshableGamesUseCase
import ca.hojat.gamehub.core.domain.games.common.RefreshUseCaseParams
import ca.hojat.gamehub.core.domain.games.common.throttling.GamesRefreshingThrottlerTools
import ca.hojat.gamehub.core.domain.games.repository.GamesRepository
import ca.hojat.gamehub.core.domain.entities.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface RefreshMostAnticipatedGamesUseCase : RefreshableGamesUseCase

@Singleton
class RefreshMostAnticipatedGamesUseCaseImpl @Inject constructor(
    private val gamesRepository: GamesRepository,
    private val throttlerTools: GamesRefreshingThrottlerTools,
    private val dispatcherProvider: DispatcherProvider,
) : RefreshMostAnticipatedGamesUseCase {

    override fun execute(params: RefreshUseCaseParams): Flow<DomainResult<List<Game>>> {
        val throttlerKey =
            throttlerTools.keyProvider.provideMostAnticipatedGamesKey(params.pagination)

        return flow {
            if (throttlerTools.throttler.canRefreshGames(throttlerKey)) {
                emit(gamesRepository.remote.getMostAnticipatedGames(params.pagination))
            }
        }
            .onEachSuccess { games ->
                gamesRepository.local.saveGames(games)
                throttlerTools.throttler.updateGamesLastRefreshTime(throttlerKey)
            }
            .flowOn(dispatcherProvider.main)
    }
}
