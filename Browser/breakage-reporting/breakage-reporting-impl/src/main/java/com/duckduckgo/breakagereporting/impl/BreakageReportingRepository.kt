

package com.duckduckgo.breakagereporting.impl

import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface BreakageReportingRepository {
    fun updateAll(
        breakageReportingEntity: BreakageReportingEntity,
    )
    fun getBreakageReportingEntity(): BreakageReportingEntity
}

class RealBreakageReportingRepository constructor(
    database: BreakageReportingDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : BreakageReportingRepository {

    private val breakageReportingDao: BreakageReportingDao = database.breakageReportingDao()
    private var breakageReportingEntity = BreakageReportingEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(breakageReportingEntity: BreakageReportingEntity) {
        breakageReportingDao.updateAll(breakageReportingEntity)
        loadToMemory()
    }

    override fun getBreakageReportingEntity(): BreakageReportingEntity {
        return breakageReportingEntity
    }

    private fun loadToMemory() {
        breakageReportingEntity =
            breakageReportingDao.get() ?: BreakageReportingEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
