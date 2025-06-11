

package com.duckduckgo.app.privacy.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class NetworkLeaderboardDao {

    @Query("select count from sites_visited")
    abstract fun sitesVisited(): LiveData<Int>

    @Transaction
    open fun incrementSitesVisited() {
        val changedRows = incrementSitesVisitedIfExists()
        if (changedRows == 0) {
            initializeSitesVisited(SitesVisitedEntity(count = 1))
        }
    }

    @Insert
    protected abstract fun initializeSitesVisited(entity: SitesVisitedEntity)

    @Query("UPDATE sites_visited SET count = count + 1")
    protected abstract fun incrementSitesVisitedIfExists(): Int

    @Transaction
    open fun incrementNetworkCount(network: String) {
        val changedRows = incrementNetworkCountIfExists(network)
        if (changedRows == 0) {
            initializeNetwork(NetworkLeaderboardEntry(network, 1))
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun initializeNetwork(leaderboardEntry: NetworkLeaderboardEntry)

    @Query("UPDATE network_leaderboard SET count = count + 1 WHERE networkName = :networkName")
    protected abstract fun incrementNetworkCountIfExists(networkName: String): Int

    @Query("select * from network_leaderboard order by count desc")
    abstract fun trackerNetworkLeaderboard(): LiveData<List<NetworkLeaderboardEntry>>
}
