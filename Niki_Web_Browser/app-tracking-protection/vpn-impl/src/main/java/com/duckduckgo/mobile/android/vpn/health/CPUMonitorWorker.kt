

package com.duckduckgo.mobile.android.vpn.health

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.app.di.ProcessName
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.pixels.DeviceShieldPixels
import javax.inject.Inject
import kotlinx.coroutines.withContext
import logcat.LogPriority
import logcat.asLog
import logcat.logcat

@ContributesWorker(AppScope::class)
class CPUMonitorWorker(
    context: Context,
    workerParams: WorkerParameters,
) : RemoteCoroutineWorker(context, workerParams) {
    @Inject
    lateinit var deviceShieldPixels: DeviceShieldPixels

    @Inject
    lateinit var cpuUsageReader: CPUUsageReader

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    @Inject @ProcessName
    lateinit var processName: String

    // TODO: move thresholds to remote config
    private val alertThresholds = listOf(30, 20, 10, 5).sortedDescending()

    override suspend fun doRemoteWork(): Result {
        logcat { "CPUMonitorWorker in process $processName" }
        return withContext(dispatcherProvider.io()) {
            try {
                val avgCPUUsagePercent = cpuUsageReader.readCPUUsage()
                alertThresholds.forEach {
                    if (avgCPUUsagePercent > it) {
                        deviceShieldPixels.sendCPUUsageAlert(it)
                        return@withContext Result.success()
                    }
                }
            } catch (e: Exception) {
                logcat(LogPriority.ERROR) { e.asLog() }
                return@withContext Result.failure()
            }

            return@withContext Result.success()
        }
    }
}
