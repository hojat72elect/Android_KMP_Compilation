

package com.duckduckgo.httpsupgrade.impl

import com.duckduckgo.common.utils.store.BinaryDataStore
import com.duckduckgo.httpsupgrade.store.HttpsBloomFilterSpec
import com.duckduckgo.httpsupgrade.store.HttpsBloomFilterSpecDao
import com.duckduckgo.httpsupgrade.store.HttpsFalsePositiveDomain
import com.duckduckgo.httpsupgrade.store.HttpsFalsePositivesDao
import com.duckduckgo.httpsupgrade.store.HttpsUpgradeDatabase
import java.io.IOException
import logcat.logcat

class HttpsDataPersister constructor(
    private val binaryDataStore: BinaryDataStore,
    private val httpsBloomSpecDao: HttpsBloomFilterSpecDao,
    private val httpsFalsePositivesDao: HttpsFalsePositivesDao,
    private val httpsUpgradeDatabase: HttpsUpgradeDatabase,
) {

    fun persistBloomFilter(
        specification: HttpsBloomFilterSpec,
        bytes: ByteArray,
        falsePositives: List<HttpsFalsePositiveDomain>,
    ) {
        runCatching {
            httpsUpgradeDatabase.runInTransaction {
                persistBloomFilter(specification, bytes)
                persistFalsePositives(falsePositives)
            }
        }
    }

    fun persistBloomFilter(
        specification: HttpsBloomFilterSpec,
        bytes: ByteArray,
    ) {
        if (!binaryDataStore.verifyCheckSum(bytes, specification.sha256)) {
            throw IOException("Https binary has incorrect sha, throwing away file")
        }

        logcat { "Updating https bloom data store with new data" }
        httpsUpgradeDatabase.runInTransaction {
            httpsBloomSpecDao.insert(specification)
            binaryDataStore.saveData(HttpsBloomFilterSpec.HTTPS_BINARY_FILE, bytes)
        }
    }

    fun persistFalsePositives(falsePositives: List<HttpsFalsePositiveDomain>) {
        httpsFalsePositivesDao.updateAll(falsePositives)
    }

    fun isPersisted(specification: HttpsBloomFilterSpec): Boolean {
        return specification == httpsBloomSpecDao.get() && binaryDataStore.verifyCheckSum(
            HttpsBloomFilterSpec.HTTPS_BINARY_FILE,
            specification.sha256,
        )
    }
}
