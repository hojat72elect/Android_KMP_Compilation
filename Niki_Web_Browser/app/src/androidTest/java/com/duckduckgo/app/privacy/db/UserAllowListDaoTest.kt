

package com.duckduckgo.app.privacy.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.common.test.CoroutineTestRule
import com.duckduckgo.common.test.blockingObserve
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserAllowListDaoTest {

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: UserAllowListDao

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.userAllowListDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun whenInitializedThenListIsEmpty() {
        assertTrue(dao.all().blockingObserve()!!.isEmpty())
    }

    @Test
    fun whenElementAddedThenListSizeIsOne() {
        dao.insert(DOMAIN)
        assertEquals(1, dao.all().blockingObserve()!!.size)
    }

    @Test
    fun whenElementAddedThenFlowListSizeIsOne() = runBlocking {
        dao.insert(DOMAIN)
        assertEquals(1, dao.allDomainsFlow().first().size)
    }

    @Test
    fun whenElementAddedThenContainsIsTrue() {
        dao.insert(DOMAIN)
        assertTrue(dao.contains(DOMAIN))
    }

    @Test
    fun wheElementDeletedThenContainsIsFalse() {
        dao.insert(DOMAIN)
        dao.delete(DOMAIN)
        assertFalse(dao.contains(DOMAIN))
    }

    @Test
    fun whenElementDoesNotExistThenContainsIsFalse() {
        assertFalse(dao.contains(DOMAIN))
    }

    companion object {
        const val DOMAIN = "www.example.com"
    }
}
