

package com.duckduckgo.app.survey.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.duckduckgo.app.survey.model.Survey

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(survey: Survey)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(survey: Survey)

    @Query("select * from survey where surveyId = :surveyId")
    fun get(surveyId: String): Survey?

    @Query("""select * from survey where status = "SCHEDULED" limit 1""")
    fun getLiveScheduled(): LiveData<Survey>

    @Query("""select * from survey where status = "SCHEDULED"""")
    fun getScheduled(): List<Survey>

    @Query("""delete from survey where status = "SCHEDULED" or status = "NOT_ALLOCATED"""")
    fun deleteUnusedSurveys()

    @Transaction
    fun cancelScheduledSurveys() {
        getScheduled().forEach {
            it.status = Survey.Status.CANCELLED
            update(it)
        }
    }
}
