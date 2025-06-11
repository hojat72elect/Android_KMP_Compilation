

package com.duckduckgo.sync.impl

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.SyncState
import com.duckduckgo.sync.api.SyncState.FAILED
import com.duckduckgo.sync.api.SyncState.OFF
import com.duckduckgo.sync.api.SyncStateMonitor
import com.duckduckgo.sync.impl.engine.SyncStateRepository
import com.duckduckgo.sync.store.SyncStore
import com.duckduckgo.sync.store.model.SyncAttempt
import com.duckduckgo.sync.store.model.SyncAttemptState.FAIL
import com.duckduckgo.sync.store.model.SyncAttemptState.IN_PROGRESS
import com.duckduckgo.sync.store.model.SyncAttemptState.SUCCESS
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

@ContributesBinding(scope = AppScope::class, boundType = SyncStateMonitor::class)
class RealSyncStateMonitor @Inject constructor(
    private val syncStore: SyncStore,
    private val syncStateRepository: SyncStateRepository,
    private val dispatcherProvider: DispatcherProvider,
) : SyncStateMonitor {

    override fun syncState(): Flow<SyncState> {
        return syncStateRepository.state()
            .combine(syncStore.isSignedInFlow()) { attempt, signedIn ->
                mapState(attempt, signedIn)
            }.distinctUntilChanged()
            .flowOn(dispatcherProvider.io())
    }

    private fun mapState(
        attempt: SyncAttempt?,
        signedIn: Boolean,
    ): SyncState {
        return if (signedIn) {
            if (attempt == null) {
                Timber.d("Sync-State: Sync Monitor signed in, Sync in READY state")
                SyncState.READY
            } else {
                Timber.d("Sync-State: Sync Monitor signed $signedIn, sync in ${attempt.state} state")
                val state = when (attempt.state) {
                    IN_PROGRESS -> SyncState.IN_PROGRESS
                    SUCCESS -> SyncState.READY
                    FAIL -> FAILED
                }
                state
            }
        } else {
            Timber.d("Sync-State: Sync Monitor not signed in, Sync in OFF state")
            return OFF
        }
    }
}
