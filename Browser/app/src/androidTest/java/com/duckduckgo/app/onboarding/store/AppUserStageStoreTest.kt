

package com.duckduckgo.app.onboarding.store

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.common.test.CoroutineTestRule
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppUserStageStoreTest {
    private lateinit var userStageDao: UserStageDao
    private lateinit var db: AppDatabase

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userStageDao = db.userStageDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun whenStageObserverAttachedThenNullReturnedByDefault() = runTest {
        assertNull(userStageDao.currentUserAppStageFlow().first())
    }

    @Test
    fun whenStageUpdatedThenNotifyObservers() = runTest {
        val expected = UserStage(appStage = AppStage.ESTABLISHED)

        userStageDao.insert(expected)

        userStageDao.currentUserAppStageFlow().test {
            assertEquals(expected, awaitItem())
        }
    }
}
