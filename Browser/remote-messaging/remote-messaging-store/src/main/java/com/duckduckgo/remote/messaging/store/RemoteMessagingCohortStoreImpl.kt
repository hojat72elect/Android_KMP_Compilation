

package com.duckduckgo.remote.messaging.store

import com.duckduckgo.common.utils.DispatcherProvider
import kotlin.random.Random
import kotlinx.coroutines.withContext

interface RemoteMessagingCohortStore {
    suspend fun getPercentile(remoteMessageId: String): Float
}

class RemoteMessagingCohortStoreImpl constructor(
    database: RemoteMessagingDatabase,
    private val dispatchers: DispatcherProvider,
) : RemoteMessagingCohortStore {

    private val cohortDao: RemoteMessagingCohortDao = database.remoteMessagingCohortDao()

    override suspend fun getPercentile(remoteMessageId: String): Float {
        return withContext(dispatchers.io()) {
            val cohort = cohortDao.messageById(remoteMessageId)
            if (cohort == null) {
                val percentile = calculatePercentile()
                val remoteMessagingCohort = RemoteMessagingCohort(messageId = remoteMessageId, percentile = percentile)
                cohortDao.insert(remoteMessagingCohort)
                return@withContext percentile
            } else {
                return@withContext cohort.percentile
            }
        }
    }

    private fun calculatePercentile(): Float {
        return Random.nextDouble(1.0).toFloat()
    }
}
