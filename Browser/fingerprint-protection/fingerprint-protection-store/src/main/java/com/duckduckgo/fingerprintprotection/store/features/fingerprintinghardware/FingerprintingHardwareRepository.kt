

package com.duckduckgo.fingerprintprotection.store.features.fingerprintinghardware

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.fingerprintprotection.store.FingerprintProtectionDatabase
import com.duckduckgo.fingerprintprotection.store.FingerprintingHardwareEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface FingerprintingHardwareRepository {
    fun updateAll(
        fingerprintingHardwareEntity: FingerprintingHardwareEntity,
    )
    var fingerprintingHardwareEntity: FingerprintingHardwareEntity
}

class RealFingerprintingHardwareRepository constructor(
    val database: FingerprintProtectionDatabase,
    val coroutineScope: CoroutineScope,
    val dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : FingerprintingHardwareRepository {

    private val fingerprintingHardwareDao: FingerprintingHardwareDao = database.fingerprintingHardwareDao()
    override var fingerprintingHardwareEntity = FingerprintingHardwareEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(fingerprintingHardwareEntity: FingerprintingHardwareEntity) {
        fingerprintingHardwareDao.updateAll(fingerprintingHardwareEntity)
        loadToMemory()
    }

    private fun loadToMemory() {
        fingerprintingHardwareEntity =
            fingerprintingHardwareDao.get() ?: FingerprintingHardwareEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
