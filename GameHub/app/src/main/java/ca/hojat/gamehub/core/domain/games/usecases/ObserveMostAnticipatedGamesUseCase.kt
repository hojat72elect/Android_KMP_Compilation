package ca.hojat.gamehub.core.domain.games.usecases

import ca.hojat.gamehub.core.domain.common.DispatcherProvider
import ca.hojat.gamehub.core.domain.games.ObservableGamesUseCase
import ca.hojat.gamehub.core.domain.games.common.ObserveUseCaseParams
import ca.hojat.gamehub.core.domain.games.repository.GamesLocalDataSource
import ca.hojat.gamehub.core.domain.entities.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface ObserveMostAnticipatedGamesUseCase : ObservableGamesUseCase

@Singleton
class ObserveMostAnticipatedGamesUseCaseImpl @Inject constructor(
    private val gamesLocalDataSource: GamesLocalDataSource,
    private val dispatcherProvider: DispatcherProvider,
) : ObserveMostAnticipatedGamesUseCase {

    override fun execute(params: ObserveUseCaseParams): Flow<List<Game>> {
        return gamesLocalDataSource.observeMostAnticipatedGames(params.pagination)
            .flowOn(dispatcherProvider.main)
    }
}
