package ca.hojat.gamehub.core.data.games.common

import ca.hojat.gamehub.core.providers.TimestampProvider
import com.paulrybitskyi.hiltbinder.BindType
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private val POPULAR_GAMES_MIN_RELEASE_DATE_DURATION =
    TimeUnit.DAYS.toSeconds(@Suppress("MagicNumber") 90L)
private val RECENTLY_RELEASED_GAMES_MIN_RELEASE_DATE_DURATION =
    TimeUnit.DAYS.toSeconds(@Suppress("MagicNumber") 30L)

interface DiscoveryGamesReleaseDatesProvider {
    fun getPopularGamesMinReleaseDate(): Long
    fun getRecentlyReleasedGamesMinReleaseDate(): Long
    fun getRecentlyReleasedGamesMaxReleaseDate(): Long
    fun getComingSoonGamesMinReleaseDate(): Long
    fun getMostAnticipatedGamesMinReleaseDate(): Long
}

@Singleton
@BindType
class DiscoveryGamesReleaseDatesProviderImpl @Inject constructor(
    private val timestampProvider: TimestampProvider,
) : DiscoveryGamesReleaseDatesProvider {

    override fun getPopularGamesMinReleaseDate(): Long {
        val currentUnixTimestamp = getUnixTimestamp()

        return (currentUnixTimestamp - POPULAR_GAMES_MIN_RELEASE_DATE_DURATION)
    }

    override fun getRecentlyReleasedGamesMinReleaseDate(): Long {
        val maxReleaseDateTimestamp = getRecentlyReleasedGamesMaxReleaseDate()

        return (maxReleaseDateTimestamp - RECENTLY_RELEASED_GAMES_MIN_RELEASE_DATE_DURATION)
    }

    override fun getRecentlyReleasedGamesMaxReleaseDate(): Long {
        return getUnixTimestamp()
    }

    override fun getComingSoonGamesMinReleaseDate(): Long {
        return getUnixTimestamp()
    }

    override fun getMostAnticipatedGamesMinReleaseDate(): Long {
        return getUnixTimestamp()
    }

    private fun getUnixTimestamp(): Long {
        return timestampProvider.getUnixTimestamp(TimeUnit.SECONDS)
    }
}
