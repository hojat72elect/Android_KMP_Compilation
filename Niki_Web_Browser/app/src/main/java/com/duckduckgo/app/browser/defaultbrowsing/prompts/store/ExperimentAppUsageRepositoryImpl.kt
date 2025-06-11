

package com.duckduckgo.app.browser.defaultbrowsing.prompts.store

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.duckduckgo.app.browser.defaultbrowsing.prompts.store.ExperimentAppUsageRepository.UserNotEnrolledException
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlinx.coroutines.withContext

@ContributesBinding(scope = AppScope::class)
@SingleInstanceIn(scope = AppScope::class)
class ExperimentAppUsageRepositoryImpl @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val experimentAppUsageDao: ExperimentAppUsageDao,
) : ExperimentAppUsageRepository {

    override suspend fun recordAppUsedNow() = withContext(dispatchers.io()) {
        val isoDateET = ZonedDateTime.now(ZoneId.of("America/New_York"))
            .truncatedTo(ChronoUnit.DAYS)
            .format(DateTimeFormatter.ISO_LOCAL_DATE)

        experimentAppUsageDao.insert(ExperimentAppUsageEntity(isoDateET))
    }

    override suspend fun getActiveDaysUsedSinceEnrollment(toggle: Toggle): Result<Long> = withContext(dispatchers.io()) {
        toggle.getCohort()?.enrollmentDateET?.let { enrollmentZonedDateTimeETString ->
            try {
                val isoDateET = ZonedDateTime.parse(enrollmentZonedDateTimeETString)
                    .truncatedTo(ChronoUnit.DAYS)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE)

                val daysUsed = experimentAppUsageDao.getNumberOfDaysAppUsedSinceDateET(isoDateET)
                Result.success(daysUsed)
            } catch (ex: DateTimeParseException) {
                Result.failure(ex)
            }
        } ?: Result.failure(UserNotEnrolledException())
    }
}

@Dao
abstract class ExperimentAppUsageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(experimentAppUsageEntity: ExperimentAppUsageEntity)

    @Query("SELECT COUNT(*) from experiment_app_usage_entity WHERE isoDateET > :isoDateET")
    abstract fun getNumberOfDaysAppUsedSinceDateET(isoDateET: String): Long
}

@Entity(tableName = "experiment_app_usage_entity")
data class ExperimentAppUsageEntity(@PrimaryKey val isoDateET: String)
