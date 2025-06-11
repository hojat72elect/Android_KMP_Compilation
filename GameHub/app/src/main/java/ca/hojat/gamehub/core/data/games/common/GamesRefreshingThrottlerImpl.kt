package ca.hojat.gamehub.core.data.games.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import ca.hojat.gamehub.core.providers.TimestampProvider
import ca.hojat.gamehub.core.domain.games.common.throttling.GamesRefreshingThrottler
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
class GamesRefreshingThrottlerImpl @Inject constructor(
    private val gamesPreferences: DataStore<Preferences>,
    private val timestampProvider: TimestampProvider
) : GamesRefreshingThrottler {

    private companion object {
        val DEFAULT_GAMES_REFRESH_TIMEOUT = TimeUnit.MINUTES.toMillis(10L)
        val COMPANY_DEVELOPED_GAMES_REFRESH_TIMEOUT = TimeUnit.DAYS.toMillis(7L)
        val SIMILAR_GAMES_REFRESH_TIMEOUT = TimeUnit.DAYS.toMillis(7L)
    }

    override suspend fun canRefreshGames(key: String): Boolean {
        return canRefreshGames(
            key = longPreferencesKey(key),
            refreshTimeout = DEFAULT_GAMES_REFRESH_TIMEOUT
        )
    }

    override suspend fun updateGamesLastRefreshTime(key: String) {
        updateGamesLastRefreshTime(longPreferencesKey(key))
    }

    override suspend fun canRefreshCompanyDevelopedGames(key: String): Boolean {
        return canRefreshGames(
            key = longPreferencesKey(key),
            refreshTimeout = COMPANY_DEVELOPED_GAMES_REFRESH_TIMEOUT
        )
    }

    override suspend fun canRefreshSimilarGames(key: String): Boolean {
        return canRefreshGames(
            key = longPreferencesKey(key),
            refreshTimeout = SIMILAR_GAMES_REFRESH_TIMEOUT
        )
    }

    private suspend fun canRefreshGames(key: Preferences.Key<Long>, refreshTimeout: Long): Boolean {
        return gamesPreferences.data
            .map { it[key] ?: 0L }
            .map { timestampProvider.getUnixTimestamp() > (it + refreshTimeout) }
            .first()
    }

    private suspend fun updateGamesLastRefreshTime(key: Preferences.Key<Long>) {
        gamesPreferences.edit {
            it[key] = timestampProvider.getUnixTimestamp()
        }
    }
}
