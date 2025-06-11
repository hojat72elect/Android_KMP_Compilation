package ca.hojat.gamehub.feature_info.domain.entities

import ca.hojat.gamehub.core.domain.entities.Game

data class InfoScreenData(
    val game: Game,
    val isGameLiked: Boolean,
    val companyGames: List<Game>,
    val similarGames: List<Game>,
)
