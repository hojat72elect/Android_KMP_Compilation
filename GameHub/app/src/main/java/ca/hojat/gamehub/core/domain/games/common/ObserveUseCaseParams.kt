package ca.hojat.gamehub.core.domain.games.common

import ca.hojat.gamehub.core.domain.entities.Pagination

data class ObserveUseCaseParams(
    val pagination: Pagination = Pagination()
)
