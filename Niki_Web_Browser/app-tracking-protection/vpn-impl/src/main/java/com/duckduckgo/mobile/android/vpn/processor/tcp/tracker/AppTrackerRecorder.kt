

package com.duckduckgo.mobile.android.vpn.processor.tcp.tracker

import androidx.annotation.WorkerThread
import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.model.VpnTracker
import com.duckduckgo.mobile.android.vpn.service.VpnServiceCallbacks
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason
import com.duckduckgo.mobile.android.vpn.stats.AppTrackerBlockingStatsRepository
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.*
import logcat.logcat

interface AppTrackerRecorder {
    fun insertTracker(vpnTracker: VpnTracker)
}

@ContributesMultibinding(
    scope = VpnScope::class,
    boundType = VpnServiceCallbacks::class,
)
@ContributesBinding(
    scope = VpnScope::class,
    boundType = AppTrackerRecorder::class,
)
@SingleInstanceIn(VpnScope::class)
class BatchedAppTrackerRecorder @Inject constructor(
    private val appTrackerBlockingRepository: AppTrackerBlockingStatsRepository,
) : VpnServiceCallbacks, AppTrackerRecorder {

    private val batchedTrackers = mutableListOf<VpnTracker>()
    private val insertionDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val periodicInsertionJob = ConflatedJob()

    override fun onVpnStarted(coroutineScope: CoroutineScope) {
        logcat { "Batched app tracker recorder starting" }

        periodicInsertionJob += coroutineScope.launch(insertionDispatcher) {
            while (isActive) {
                flushInMemoryTrackersToDatabase()
                delay(Random.nextLong(PERIODIC_INSERTION_FREQUENCY_MS))
            }
        }
    }

    override fun onVpnStopped(
        coroutineScope: CoroutineScope,
        vpnStopReason: VpnStopReason,
    ) {
        logcat { "Batched app tracker recorder stopped" }
        periodicInsertionJob.cancel()
        coroutineScope.launch(insertionDispatcher) {
            flushInMemoryTrackersToDatabase()
        }
    }

    @WorkerThread
    private fun flushInMemoryTrackersToDatabase() {
        val toInsert = mutableListOf<VpnTracker>()
        synchronized(batchedTrackers) {
            if (batchedTrackers.isEmpty()) {
                return
            }
            toInsert.addAll(batchedTrackers)
            batchedTrackers.clear()
        }

        appTrackerBlockingRepository.insert(toInsert)
        logcat { "Inserted ${toInsert.size} trackers from memory into db" }
    }

    override fun insertTracker(vpnTracker: VpnTracker) {
        synchronized(batchedTrackers) {
            batchedTrackers.add(vpnTracker)
        }
    }

    companion object {
        private const val PERIODIC_INSERTION_FREQUENCY_MS: Long = 1_000
    }
}
