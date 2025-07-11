

package com.duckduckgo.fingerprintprotection.store.features.fingerprintingcanvas

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.fingerprintprotection.store.FingerprintProtectionDatabase
import com.duckduckgo.fingerprintprotection.store.FingerprintingCanvasEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface FingerprintingCanvasRepository {
    fun updateAll(
        fingerprintingCanvasEntity: FingerprintingCanvasEntity,
    )
    var fingerprintingCanvasEntity: FingerprintingCanvasEntity
}

class RealFingerprintingCanvasRepository(
    val database: FingerprintProtectionDatabase,
    val coroutineScope: CoroutineScope,
    val dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : FingerprintingCanvasRepository {

    private val fingerprintingCanvasDao: FingerprintingCanvasDao = database.fingerprintingCanvasDao()
    override var fingerprintingCanvasEntity = FingerprintingCanvasEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(fingerprintingCanvasEntity: FingerprintingCanvasEntity) {
        fingerprintingCanvasDao.updateAll(fingerprintingCanvasEntity)
        loadToMemory()
    }

    private fun loadToMemory() {
        fingerprintingCanvasEntity =
            fingerprintingCanvasDao.get() ?: FingerprintingCanvasEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
