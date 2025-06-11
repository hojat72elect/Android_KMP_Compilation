

package com.duckduckgo.fingerprintprotection.store.features.fingerprintingbattery

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.fingerprintprotection.store.FingerprintProtectionDatabase
import com.duckduckgo.fingerprintprotection.store.FingerprintingBatteryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface FingerprintingBatteryRepository {
    fun updateAll(
        fingerprintingBatteryEntity: FingerprintingBatteryEntity,
    )
    var fingerprintingBatteryEntity: FingerprintingBatteryEntity
}

class RealFingerprintingBatteryRepository constructor(
    val database: FingerprintProtectionDatabase,
    val coroutineScope: CoroutineScope,
    val dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : FingerprintingBatteryRepository {

    private val fingerprintingBatteryDao: FingerprintingBatteryDao = database.fingerprintingBatteryDao()
    override var fingerprintingBatteryEntity = FingerprintingBatteryEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(fingerprintingBatteryEntity: FingerprintingBatteryEntity) {
        fingerprintingBatteryDao.updateAll(fingerprintingBatteryEntity)
        loadToMemory()
    }

    private fun loadToMemory() {
        fingerprintingBatteryEntity =
            fingerprintingBatteryDao.get() ?: FingerprintingBatteryEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
