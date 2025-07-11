

package com.duckduckgo.privacy.config.store.features.trackingparameters

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.feature.toggles.api.FeatureException
import com.duckduckgo.privacy.config.store.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface TrackingParametersRepository {
    fun updateAll(exceptions: List<TrackingParameterExceptionEntity>, parameters: List<TrackingParameterEntity>)
    val exceptions: List<FeatureException>
    val parameters: List<String>
}

class RealTrackingParametersRepository(
    val database: PrivacyConfigDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : TrackingParametersRepository {

    private val trackingParametersDao: TrackingParametersDao = database.trackingParametersDao()

    override val exceptions = CopyOnWriteArrayList<FeatureException>()
    override val parameters = CopyOnWriteArrayList<String>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(
        exceptions: List<TrackingParameterExceptionEntity>,
        parameters: List<TrackingParameterEntity>,
    ) {
        trackingParametersDao.updateAll(exceptions, parameters)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        trackingParametersDao.getAllExceptions().map {
            exceptions.add(it.toFeatureException())
        }

        parameters.clear()
        trackingParametersDao.getAllTrackingParameters().map {
            parameters.add(it.parameter)
        }
    }
}
