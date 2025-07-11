

package com.duckduckgo.pir.internal.scan

import android.content.Context
import android.os.Process
import androidx.work.WorkerParameters
import androidx.work.multiprocess.RemoteCoroutineWorker
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.pir.internal.common.PirActionsRunnerFactory.RunType.SCHEDULED
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import logcat.logcat

@ContributesWorker(AppScope::class)
class PirScheduledScanRemoteWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
) : RemoteCoroutineWorker(context, workerParameters) {
    @Inject
    lateinit var pirScan: PirScan

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private val serviceScope by lazy { CoroutineScope(dispatcherProvider.io() + SupervisorJob()) }

    override suspend fun doRemoteWork(): Result {
        logcat { "PIR-WORKER ($this}: doRemoteWork ${Process.myPid()}" }
        val result = pirScan.execute(supportedBrokers, context.applicationContext, SCHEDULED, serviceScope)

        return if (result.isSuccess) {
            logcat { "PIR-WORKER ($this}: Successfully completed!" }
            Result.success()
        } else {
            logcat { "PIR-WORKER ($this}: Failed to complete." }
            Result.failure()
        }
    }

    companion object {
        internal const val TAG_SCHEDULED_SCAN = "TAG-PIR-SCHEDULED-SCAN"
    }
}
