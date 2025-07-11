

package com.duckduckgo.pir.internal.callbacks

import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.pir.internal.pixels.PirPixelSender
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.asLog
import logcat.logcat

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PirCallbacks::class,
)
@SingleInstanceIn(AppScope::class)
class PirCpuMonitor @Inject constructor(
    private val pixelSender: PirPixelSender,
    private val dispatcherProvider: DispatcherProvider,
    private val cpuUsageReader: CPUUsageReader,
) : PirCallbacks {
    private val alertThresholds = listOf(30, 20, 10).sortedDescending()
    private val monitorJob = ConflatedJob()

    override fun onPirJobStarted(coroutineScope: CoroutineScope) {
        monitorJob += coroutineScope.launch(dispatcherProvider.io()) {
            logcat { "PIR-MONITOR: ${this@PirCpuMonitor} onPirJobStarted " }
            delay(10_000) // Add delay before measuring
            while (isActive) {
                try {
                    val avgCPUUsagePercent = cpuUsageReader.readCPUUsage()
                    logcat { "PIR-MONITOR: avgCPUUsagePercent: $avgCPUUsagePercent on ${android.os.Process.myPid()} " }
                    // If any threshold has been reached, we will a emit a pixel.
                    alertThresholds.forEach {
                        if (avgCPUUsagePercent > it) {
                            pixelSender.sendCPUUsageAlert(it)
                        }
                    }
                    delay(60_000)
                } catch (e: Exception) {
                    logcat(LogPriority.ERROR) { e.asLog() }
                    monitorJob.cancel()
                }
            }
        }
    }

    override fun onPirJobCompleted() {
        logcat { "PIR-MONITOR: ${this@PirCpuMonitor} onPirJobCompleted" }
        monitorJob.cancel()
    }

    override fun onPirJobStopped() {
        logcat { "PIR-MONITOR: ${this@PirCpuMonitor} onPirJobStopped" }
        monitorJob.cancel()
    }
}
