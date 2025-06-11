

package com.duckduckgo.app.global.events.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.common.test.CoroutineTestRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserEventsDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    private lateinit var db: AppDatabase

    private lateinit var dao: UserEventsDao

    private lateinit var testee: AppUserEventsStore

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java).build()
        dao = db.userEventsDao()
        testee = AppUserEventsStore(dao, coroutineRule.testDispatcherProvider)
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun whenGetUserEventAndDatabaseEmptyThenReturnNull() = runTest {
        assertNull(testee.getUserEvent(UserEventKey.FIRE_BUTTON_EXECUTED))
    }

    @Test
    fun whenInsertingUserEventThenTimestampIsNotNull() = runTest {
        testee.registerUserEvent(UserEventKey.FIRE_BUTTON_EXECUTED)

        assertNotNull(testee.getUserEvent(UserEventKey.FIRE_BUTTON_EXECUTED)?.timestamp)
    }

    @Test
    fun whenInsertingSameUserEventThenReplaceOldTimestamp() = runTest {
        testee.registerUserEvent(UserEventKey.FIRE_BUTTON_EXECUTED)
        val timestamp = testee.getUserEvent(UserEventKey.FIRE_BUTTON_EXECUTED)?.timestamp

        testee.registerUserEvent(UserEventKey.FIRE_BUTTON_EXECUTED)

        assertNotEquals(timestamp, testee.getUserEvent(UserEventKey.FIRE_BUTTON_EXECUTED)?.timestamp)
    }
}
