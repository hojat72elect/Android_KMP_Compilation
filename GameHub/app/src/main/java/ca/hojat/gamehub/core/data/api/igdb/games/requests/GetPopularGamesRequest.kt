package ca.hojat.gamehub.core.data.api.igdb.games.requests

import ca.hojat.gamehub.core.data.api.common.ApiRequest

data class GetPopularGamesRequest(
    val minReleaseDateTimestamp: Long,
    override val offset: Int,
    override val limit: Int,
) : ApiRequest
