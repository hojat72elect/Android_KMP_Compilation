

package com.duckduckgo.app.cta.db

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.duckduckgo.app.cta.model.CtaId
import com.duckduckgo.app.cta.model.DismissedCta
import com.duckduckgo.app.global.db.AppDatabase
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DismissedCtaDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: DismissedCtaDao

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, AppDatabase::class.java).build()
        dao = db.dismissedCtaDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun whenCtaNotInsertedThenEntryDoesNotExist() {
        assertFalse(dao.exists(CtaId.ADD_WIDGET))
    }

    @Test
    fun whenInsertedThenEntryExists() {
        dao.insert(DismissedCta(CtaId.ADD_WIDGET))
        assertTrue(dao.exists(CtaId.ADD_WIDGET))
    }
}
