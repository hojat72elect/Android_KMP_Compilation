

package com.duckduckgo.networkprotection.store

import androidx.annotation.WorkerThread
import com.duckduckgo.networkprotection.store.db.NetPExclusionListDao
import com.duckduckgo.networkprotection.store.db.NetPManuallyExcludedApp
import kotlinx.coroutines.flow.Flow

@WorkerThread
interface NetPManualExclusionListRepository {
    fun getManualAppExclusionList(): List<NetPManuallyExcludedApp>

    fun getManualAppExclusionListFlow(): Flow<List<NetPManuallyExcludedApp>>

    fun manuallyExcludeApp(packageName: String)

    fun manuallyExcludeApps(packageNames: List<String>)

    fun manuallyEnableApp(packageName: String)

    fun manuallyEnableApps(packageNames: List<String>)

    fun restoreDefaultProtectedList()
}

class RealNetPManualExclusionListRepository constructor(
    private val exclusionListDao: NetPExclusionListDao,
) : NetPManualExclusionListRepository {
    override fun getManualAppExclusionList(): List<NetPManuallyExcludedApp> = exclusionListDao.getManualAppExclusionList()

    override fun getManualAppExclusionListFlow(): Flow<List<NetPManuallyExcludedApp>> = exclusionListDao.getManualAppExclusionListFlow()

    override fun manuallyExcludeApp(packageName: String) {
        exclusionListDao.insertIntoManualAppExclusionList(NetPManuallyExcludedApp(packageId = packageName, isProtected = false))
    }

    override fun manuallyExcludeApps(packageNames: List<String>) {
        packageNames.map {
            NetPManuallyExcludedApp(packageId = it, isProtected = false)
        }.also {
            exclusionListDao.insertIntoManualAppExclusionList(it)
        }
    }

    override fun manuallyEnableApp(packageName: String) {
        exclusionListDao.insertIntoManualAppExclusionList(NetPManuallyExcludedApp(packageId = packageName, isProtected = true))
    }

    override fun manuallyEnableApps(packageNames: List<String>) {
        packageNames.map {
            NetPManuallyExcludedApp(packageId = it, isProtected = true)
        }.also {
            exclusionListDao.insertIntoManualAppExclusionList(it)
        }
    }

    override fun restoreDefaultProtectedList() {
        exclusionListDao.deleteManualAppExclusionList()
    }
}
