

package com.duckduckgo.app.privacy.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.common.test.blockingObserve
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NetworkLeaderboardDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: NetworkLeaderboardDao

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        db = Room
            .inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.networkLeaderboardDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun whenNetworkThatDoesNotExistIncrementedThenAddedToDatabase() {
        dao.incrementNetworkCount("Network1")
        val data: List<NetworkLeaderboardEntry>? = dao.trackerNetworkLeaderboard().blockingObserve()
        assertEquals(1, data!!.size)
        assertTrue(data.contains(NetworkLeaderboardEntry("Network1", 1)))
    }

    @Test
    fun whenNetworksIncrementedMultipleTimesThenReturnedWithCountInDescendingOrder() {
        dao.incrementNetworkCount("Network1")
        dao.incrementNetworkCount("Network2")
        dao.incrementNetworkCount("Network2")
        dao.incrementNetworkCount("Network2")
        dao.incrementNetworkCount("Network3")
        dao.incrementNetworkCount("Network3")

        val data: List<NetworkLeaderboardEntry> = dao.trackerNetworkLeaderboard().blockingObserve()!!
        assertEquals(NetworkLeaderboardEntry("Network2", 3), data[0])
        assertEquals(NetworkLeaderboardEntry("Network3", 2), data[1])
        assertEquals(NetworkLeaderboardEntry("Network1", 1), data[2])
    }

    @Test
    fun whenSitesVisitedIncrementedThenSiteVisitedCountIncreasesByOne() {
        dao.incrementSitesVisited()
        assertEquals(1, dao.sitesVisited().blockingObserve())
        dao.incrementSitesVisited()
        assertEquals(2, dao.sitesVisited().blockingObserve())
    }
}
