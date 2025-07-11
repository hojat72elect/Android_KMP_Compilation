

package com.duckduckgo.app.usage.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

class AppDaysUsedDatabaseRepository(private val appDaysUsedDao: AppDaysUsedDao) : AppDaysUsedRepository {

    private val singleThreadedDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    override suspend fun recordAppUsedToday() = withContext(singleThreadedDispatcher) {
        val today = formatter.format((Date()))
        when (val lastActiveDay = appDaysUsedDao.getLastDayAppUsed()?.date) {
            null -> appDaysUsedDao.insert(AppDaysUsedEntity())
            today -> appDaysUsedDao.insert(AppDaysUsedEntity(previousDate = today))
            else -> appDaysUsedDao.insert(AppDaysUsedEntity(previousDate = lastActiveDay))
        }
    }

    override suspend fun getNumberOfDaysAppUsed(): Long {
        return withContext(singleThreadedDispatcher) {
            return@withContext appDaysUsedDao.getNumberOfDaysAppUsed()
        }
    }

    override suspend fun getNumberOfDaysAppUsedSinceDate(date: Date): Long {
        return withContext(singleThreadedDispatcher) {
            return@withContext appDaysUsedDao.getNumberOfDaysAppUsedSince(formatter.format(date))
        }
    }

    override suspend fun getLastActiveDay(): String {
        return withContext(singleThreadedDispatcher) {
            return@withContext appDaysUsedDao.getLastDayAppUsed()?.date ?: formatter.format((Date()))
        }
    }

    override suspend fun getPreviousActiveDay(): String? {
        return withContext(singleThreadedDispatcher) {
            return@withContext appDaysUsedDao.getLastDayAppUsed()?.previousDate
        }
    }
}

@Entity(tableName = "app_days_used")
data class AppDaysUsedEntity(
    @PrimaryKey val date: String = formatter.format((Date())),
    @ColumnInfo(name = "previous_date")
    val previousDate: String? = null,
)
