

package com.duckduckgo.app.onboarding.store

import com.duckduckgo.common.utils.DispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface UserStageStore {
    fun userAppStageFlow(): Flow<AppStage>
    suspend fun getUserAppStage(): AppStage
    suspend fun stageCompleted(appStage: AppStage): AppStage
    suspend fun moveToStage(appStage: AppStage)
    val currentAppStage: Flow<AppStage>
}

class AppUserStageStore @Inject constructor(
    private val userStageDao: UserStageDao,
    private val dispatcher: DispatcherProvider,
) : UserStageStore {

    override fun userAppStageFlow(): Flow<AppStage> {
        return userStageDao.currentUserAppStageFlow().map { it?.appStage ?: AppStage.NEW }
    }

    override suspend fun getUserAppStage(): AppStage {
        return withContext(dispatcher.io()) {
            val userStage = userStageDao.currentUserAppStage()
            return@withContext userStage?.appStage ?: AppStage.NEW
        }
    }

    override suspend fun stageCompleted(appStage: AppStage): AppStage {
        return withContext(dispatcher.io()) {
            val newAppStage = when (appStage) {
                AppStage.NEW -> AppStage.DAX_ONBOARDING
                AppStage.DAX_ONBOARDING -> AppStage.ESTABLISHED
                AppStage.ESTABLISHED -> AppStage.ESTABLISHED
            }

            if (newAppStage != appStage) {
                userStageDao.updateUserStage(newAppStage)
            }

            return@withContext newAppStage
        }
    }

    override suspend fun moveToStage(appStage: AppStage) {
        userStageDao.updateUserStage(appStage)
    }

    override val currentAppStage: Flow<AppStage> = userStageDao.currentAppStage().map { it.appStage }
}

suspend fun UserStageStore.isNewUser(): Boolean {
    return this.getUserAppStage() == AppStage.NEW
}

suspend fun UserStageStore.daxOnboardingActive(): Boolean {
    return this.getUserAppStage() == AppStage.DAX_ONBOARDING
}
