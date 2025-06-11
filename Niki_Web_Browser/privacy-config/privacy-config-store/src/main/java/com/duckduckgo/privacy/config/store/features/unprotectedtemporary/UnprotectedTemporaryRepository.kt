

package com.duckduckgo.privacy.config.store.features.unprotectedtemporary

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.feature.toggles.api.FeatureException
import com.duckduckgo.privacy.config.store.PrivacyConfigDatabase
import com.duckduckgo.privacy.config.store.UnprotectedTemporaryEntity
import com.duckduckgo.privacy.config.store.toFeatureException
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface UnprotectedTemporaryRepository {
    fun updateAll(exceptions: List<UnprotectedTemporaryEntity>)
    val exceptions: CopyOnWriteArrayList<FeatureException>
}

class RealUnprotectedTemporaryRepository(
    val database: PrivacyConfigDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : UnprotectedTemporaryRepository {

    private val unprotectedTemporaryDao: UnprotectedTemporaryDao =
        database.unprotectedTemporaryDao()
    override val exceptions = CopyOnWriteArrayList<FeatureException>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(exceptions: List<UnprotectedTemporaryEntity>) {
        unprotectedTemporaryDao.updateAll(exceptions)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        unprotectedTemporaryDao.getAll().map { exceptions.add(it.toFeatureException()) }
    }
}
