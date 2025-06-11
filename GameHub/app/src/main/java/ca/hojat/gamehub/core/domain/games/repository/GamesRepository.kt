package ca.hojat.gamehub.core.domain.games.repository

import javax.inject.Inject

class GamesRepository @Inject constructor(
    val local: GamesLocalDataSource,
    val remote: GamesRemoteDataSource
)
