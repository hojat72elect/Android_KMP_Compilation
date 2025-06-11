package ca.hojat.gamehub.core.domain.games.repository

import ca.hojat.gamehub.core.domain.entities.Pagination
import ca.hojat.gamehub.core.domain.entities.Company
import ca.hojat.gamehub.core.domain.entities.Game
import kotlinx.coroutines.flow.Flow

interface GamesLocalDataSource {
    suspend fun saveGames(games: List<Game>)
    suspend fun getGame(id: Int): Game?
    suspend fun getCompanyDevelopedGames(company: Company, pagination: Pagination): List<Game>
    suspend fun getSimilarGames(game: Game, pagination: Pagination): List<Game>
    suspend fun searchGames(searchQuery: String, pagination: Pagination): List<Game>

    fun observePopularGames(pagination: Pagination): Flow<List<Game>>
    fun observeRecentlyReleasedGames(pagination: Pagination): Flow<List<Game>>
    fun observeComingSoonGames(pagination: Pagination): Flow<List<Game>>
    fun observeMostAnticipatedGames(pagination: Pagination): Flow<List<Game>>
}
