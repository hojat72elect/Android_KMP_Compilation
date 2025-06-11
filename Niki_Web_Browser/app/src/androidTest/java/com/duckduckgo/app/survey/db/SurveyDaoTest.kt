

package com.duckduckgo.app.survey.db

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.app.survey.model.Survey
import com.duckduckgo.app.survey.model.Survey.Status.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SurveyDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: SurveyDao

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(getInstrumentation().targetContext, AppDatabase::class.java).build()
        dao = db.surveyDao()
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun whenSurveyNotInsertedThenItDoesNotExist() {
        assertEquals(dao.get("1"), null)
    }

    @Test
    fun whenSurveyInsertedThenItExists() {
        val survey = Survey("1", "", null, SCHEDULED)
        dao.insert(survey)
        assertEquals(survey, dao.get("1"))
    }

    @Test
    fun whenSurveyUpdatedThenRecordIsUpdated() {
        val original = Survey("1", "https://abc.com", null, SCHEDULED)
        val updated = Survey("1", "https://xyz.com", null, CANCELLED)

        dao.insert(original)
        dao.update(updated)

        assertNotSame(original, dao.get("1"))
        assertEquals(updated, dao.get("1"))
    }

    @Test
    fun whenNoSurveysThenGetScheduledIsEmpty() {
        assertEquals(0, dao.getScheduled().size)
    }

    @Test
    fun whenScheduledSurveysExistThenGetScheduledContainsThem() {
        dao.insert(Survey("1", "", null, SCHEDULED))
        dao.insert(Survey("2", "", null, SCHEDULED))
        dao.insert(Survey("3", "", null, DONE))
        dao.insert(Survey("4", "", null, CANCELLED))
        assertEquals(2, dao.getScheduled().size)
    }

    @Test
    fun whenScheduledSurveysAreCancelledTheirStatusIsUpdatedAndGetScheduledIsEmpty() {
        dao.insert(Survey("1", "", null, SCHEDULED))
        dao.insert(Survey("2", "", null, SCHEDULED))
        dao.cancelScheduledSurveys()
        assertEquals(CANCELLED, dao.get("1")?.status)
        assertEquals(CANCELLED, dao.get("2")?.status)
        assertTrue(dao.getScheduled().isEmpty())
    }

    @Test
    fun whenUnusedSurveysAreDeletedThenTheyNoLongerExistAndGetScheduledIsEmpty() {
        dao.insert(Survey("1", "", null, SCHEDULED))
        dao.insert(Survey("2", "", null, NOT_ALLOCATED))
        dao.deleteUnusedSurveys()
        assertEquals(dao.get("1"), null)
        assertEquals(dao.get("2"), null)
        assertTrue(dao.getScheduled().isEmpty())
    }
}
