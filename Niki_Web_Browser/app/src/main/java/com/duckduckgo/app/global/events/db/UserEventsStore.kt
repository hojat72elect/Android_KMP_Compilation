

package com.duckduckgo.app.global.events.db

import com.duckduckgo.common.utils.DispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface UserEventsStore {
    suspend fun getUserEvent(userEventKey: UserEventKey): UserEventEntity?
    suspend fun registerUserEvent(userEventKey: UserEventKey)
    suspend fun registerUserEvent(userEventEntity: UserEventEntity)
    suspend fun removeUserEvent(userEventKey: UserEventKey)
}

class AppUserEventsStore @Inject constructor(
    private val userEventsDao: UserEventsDao,
    private val dispatcher: DispatcherProvider,
) : UserEventsStore {

    override suspend fun getUserEvent(userEventKey: UserEventKey): UserEventEntity? {
        return withContext(dispatcher.io()) {
            userEventsDao.getUserEvent(userEventKey)
        }
    }

    override suspend fun registerUserEvent(userEventKey: UserEventKey) {
        withContext(dispatcher.io()) {
            registerUserEvent(UserEventEntity(userEventKey))
        }
    }

    override suspend fun registerUserEvent(userEventEntity: UserEventEntity) {
        withContext(dispatcher.io()) {
            userEventsDao.insert(userEventEntity)
        }
    }

    override suspend fun removeUserEvent(userEventKey: UserEventKey) {
        withContext(dispatcher.io()) {
            userEventsDao.delete(userEventKey)
        }
    }
}
