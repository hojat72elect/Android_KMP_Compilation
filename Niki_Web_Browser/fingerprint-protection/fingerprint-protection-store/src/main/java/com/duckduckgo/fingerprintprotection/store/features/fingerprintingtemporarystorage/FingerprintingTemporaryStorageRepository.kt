

package com.duckduckgo.fingerprintprotection.store.features.fingerprintingtemporarystorage

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.fingerprintprotection.store.FingerprintProtectionDatabase
import com.duckduckgo.fingerprintprotection.store.FingerprintingTemporaryStorageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface FingerprintingTemporaryStorageRepository {
    fun updateAll(
        fingerprintingTemporaryStorageEntity: FingerprintingTemporaryStorageEntity,
    )
    var fingerprintingTemporaryStorageEntity: FingerprintingTemporaryStorageEntity
}

class RealFingerprintingTemporaryStorageRepository constructor(
    val database: FingerprintProtectionDatabase,
    val coroutineScope: CoroutineScope,
    val dispatcherProvider: DispatcherProvider,
    isMainProcess: Boolean,
) : FingerprintingTemporaryStorageRepository {

    private val fingerprintingTemporaryStorageDao: FingerprintingTemporaryStorageDao = database.fingerprintingTemporaryStorageDao()
    override var fingerprintingTemporaryStorageEntity = FingerprintingTemporaryStorageEntity(json = EMPTY_JSON)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAll(fingerprintingTemporaryStorageEntity: FingerprintingTemporaryStorageEntity) {
        fingerprintingTemporaryStorageDao.updateAll(fingerprintingTemporaryStorageEntity)
        loadToMemory()
    }

    private fun loadToMemory() {
        fingerprintingTemporaryStorageEntity =
            fingerprintingTemporaryStorageDao.get() ?: FingerprintingTemporaryStorageEntity(json = EMPTY_JSON)
    }

    companion object {
        const val EMPTY_JSON = "{}"
    }
}
