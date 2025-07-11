

package com.duckduckgo.user.agent.store

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.feature.toggles.api.FeatureException
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface UserAgentRepository {
    fun updateAll(exceptions: List<UserAgentExceptionEntity>)
    val exceptions: CopyOnWriteArrayList<FeatureException>
}

class RealUserAgentRepository(
    val database: UserAgentDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : UserAgentRepository {

    private val userAgentExceptionsDao: UserAgentExceptionsDao = database.userAgentExceptionsDao()
    override val exceptions = CopyOnWriteArrayList<FeatureException>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(exceptions: List<UserAgentExceptionEntity>) {
        userAgentExceptionsDao.updateAll(exceptions)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        userAgentExceptionsDao.getAll().map { exceptions.add(it.toFeatureException()) }
    }
}
