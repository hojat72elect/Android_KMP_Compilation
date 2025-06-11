package ca.hojat.gamehub.core.data.api.igdb.games

import ca.hojat.gamehub.core.data.api.igdbcalypse.querybuilder.ApicalypseQueryBuilderFactory
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame.Schema.HYPE_COUNT
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame.Schema.ID
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame.Schema.RELEASE_DATE
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame.Schema.TOTAL_RATING
import ca.hojat.gamehub.core.data.api.igdb.games.entities.ApiGame.Schema.USERS_RATING
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetComingSoonGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetMostAnticipatedGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetPopularGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.GetRecentlyReleasedGamesRequest
import ca.hojat.gamehub.core.data.api.igdb.games.requests.SearchGamesRequest
import ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.ApicalypseSerializer
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface IgdbApiQueryFactory {
    fun createGamesSearchingQuery(request: SearchGamesRequest): String
    fun createPopularGamesRetrievalQuery(request: GetPopularGamesRequest): String
    fun createRecentlyReleasedGamesRetrievalQuery(request: GetRecentlyReleasedGamesRequest): String
    fun createComingSoonGamesRetrievalQuery(request: GetComingSoonGamesRequest): String
    fun createMostAnticipatedGamesRetrievalQuery(request: GetMostAnticipatedGamesRequest): String
    fun createGamesRetrievalQuery(request: GetGamesRequest): String
}

@BindType
class IgdbApiQueryFactoryImpl @Inject constructor(
    private val apicalypseQueryBuilderFactory: ApicalypseQueryBuilderFactory,
    private val apicalypseSerializer: ApicalypseSerializer,
) : IgdbApiQueryFactory {

    private val gameEntityFields by lazy {
        apicalypseSerializer.serialize(ApiGame::class.java)
    }

    override fun createGamesSearchingQuery(request: SearchGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .search(request.searchQuery)
            .select(gameEntityFields)
            .offset(request.offset)
            .limit(request.limit)
            .build()
    }

    override fun createPopularGamesRetrievalQuery(request: GetPopularGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                USERS_RATING.isNotNull and
                        { RELEASE_DATE.isLargerThan(request.minReleaseDateTimestamp.toString()) }
            }
            .offset(request.offset)
            .limit(request.limit)
            .sortDesc(TOTAL_RATING)
            .build()
    }

    override fun createRecentlyReleasedGamesRetrievalQuery(request: GetRecentlyReleasedGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                RELEASE_DATE.isLargerThan(request.minReleaseDateTimestamp.toString()) and
                        { RELEASE_DATE.isSmallerThan(request.maxReleaseDateTimestamp.toString()) }
            }
            .offset(request.offset)
            .limit(request.limit)
            .sortDesc(RELEASE_DATE)
            .build()
    }

    override fun createComingSoonGamesRetrievalQuery(request: GetComingSoonGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where { RELEASE_DATE.isLargerThan(request.minReleaseDateTimestamp.toString()) }
            .offset(request.offset)
            .limit(request.limit)
            .sortAsc(RELEASE_DATE)
            .build()
    }

    override fun createMostAnticipatedGamesRetrievalQuery(request: GetMostAnticipatedGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                RELEASE_DATE.isLargerThan(request.minReleaseDateTimestamp.toString()) and
                        { HYPE_COUNT.isNotNull }
            }
            .offset(request.offset)
            .limit(request.limit)
            .sortDesc(HYPE_COUNT)
            .build()
    }

    override fun createGamesRetrievalQuery(request: GetGamesRequest): String {
        val stringifiedGameIds = request.gameIds.map(Integer::toString)

        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where { ID.containsAnyOf(stringifiedGameIds) }
            .offset(request.offset)
            .limit(request.limit)
            .build()
    }
}
