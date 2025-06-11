

package com.duckduckgo.sync.impl.engine

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.impl.engine.SyncOperation.DISCARD
import com.duckduckgo.sync.impl.engine.SyncOperation.EXECUTE
import com.duckduckgo.sync.store.model.SyncAttemptState.FAIL
import com.duckduckgo.sync.store.model.SyncAttemptState.IN_PROGRESS
import com.squareup.anvil.annotations.ContributesBinding
import java.time.Duration
import java.time.OffsetDateTime
import javax.inject.Inject
import timber.log.Timber

interface SyncScheduler {

    fun scheduleOperation(): SyncOperation
}

@ContributesBinding(scope = AppScope::class)
class RealSyncScheduler @Inject constructor(private val syncStateRepository: SyncStateRepository) : SyncScheduler {
    override fun scheduleOperation(): SyncOperation {
        // for non-immediate sync operations we apply a debounce of 10 minutes.
        // on a rudimentary level, without using coroutines
        // we only allow sync operations if the last sync happened more than 10 minutes ago.
        val lastSync = syncStateRepository.current()
        return if (lastSync != null) {
            when (lastSync.state) {
                IN_PROGRESS -> DISCARD
                FAIL -> EXECUTE
                else -> {
                    val syncTime = OffsetDateTime.parse(lastSync.timestamp)
                    val now = OffsetDateTime.now()

                    val minutesAgo = Duration.between(syncTime, now).toMinutes()
                    Timber.d("Sync-Engine: Last sync was $minutesAgo minutes ago")
                    if (minutesAgo > DEBOUNCE_PERIOD_IN_MINUTES) {
                        EXECUTE
                    } else {
                        DISCARD
                    }
                }
            }
        } else {
            EXECUTE
        }
    }

    companion object {
        const val DEBOUNCE_PERIOD_IN_MINUTES = 10
    }
}

enum class SyncOperation {
    DISCARD,
    EXECUTE,
}
