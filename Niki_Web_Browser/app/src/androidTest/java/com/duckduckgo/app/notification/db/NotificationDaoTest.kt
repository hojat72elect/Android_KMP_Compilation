

package com.duckduckgo.app.notification.db

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.app.notification.model.Notification
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NotificationDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: NotificationDao

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java).build()
        dao = db.notificationDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun whenNotInsertedThenEntryDoesNotExist() {
        assertFalse(dao.exists("abc"))
    }

    @Test
    fun whenInsertedThenEntryExists() {
        dao.insert(Notification("abc"))
        assertTrue(dao.exists("abc"))
    }
}
