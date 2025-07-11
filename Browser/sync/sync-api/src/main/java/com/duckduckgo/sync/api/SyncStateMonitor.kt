

package com.duckduckgo.sync.api

import kotlinx.coroutines.flow.Flow

interface SyncStateMonitor {

    /**
     * Returns a flow of Sync state changes
     * It follows the following truth table:
     * * when the user is not signed with Sync is disabled the flow will emit a [SyncState.OFF]
     * * else it will map the state to the last [SyncAttempt]
     */
    fun syncState(): Flow<SyncState>
}

/**
 * Represent each possible sync state
 * See Tech Design: Sync Engine https://app.asana.com/0/481882893211075/1204303361994831/f
 * [READY] -> Sync is enabled, data will sync according to the specifications above. Next state can be [IN_PROGRESS] if a new sync operation stars,
 * or [OFF] if user disables Sync.
 * [IN_PROGRESS] -> Sync operation in progress. Next state can be [FAILED] if operation fails or [READY] if operation succeeds
 * [FAILED] -> Last Sync operation failed. Next state can be [IN_PROGRESS] if a new operation starts or [OFF] if user disables Sync.
 * [OFF] -> Sync is disabled. Next state can be [READY] if the user enabled sync for this device.
 */
enum class SyncState {
    READY,
    IN_PROGRESS,
    FAILED,
    OFF,
}
