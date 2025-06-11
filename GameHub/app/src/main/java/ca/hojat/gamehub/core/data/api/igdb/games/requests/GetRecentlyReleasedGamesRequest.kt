package ca.hojat.gamehub.core.data.api.igdb.games.requests

import ca.hojat.gamehub.core.data.api.common.ApiRequest

data class GetRecentlyReleasedGamesRequest(
    val minReleaseDateTimestamp: Long,
    val maxReleaseDateTimestamp: Long,
    override val offset: Int,
    override val limit: Int,
) : ApiRequest
