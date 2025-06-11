

package com.duckduckgo.sync.api.engine

interface SyncableDataPersister {
    /**
     * Sync has been enabled
     * This is an opportunity for Features to do some local initialization if needed
     */
    fun onSyncEnabled()

    /**
     * Changes from Sync Client have been received
     * Each feature is responsible for merging and solving conflicts
     */
    fun onSuccess(
        changes: SyncChangesResponse,
        conflictResolution: SyncConflictResolution,
    ): SyncMergeResult

    /**
     * Syncing data failed due to an error related to this feature
     */
    fun onError(error: SyncErrorResponse)

    /**
     * Sync Feature has been disabled / device has been removed
     * This is an opportunity for Features to do some local cleanup if needed
     */
    fun onSyncDisabled()

    /**
     * Represent each possible conflict resolution strategy that Sync supports
     * See Tech Design: Sync Engine https://app.asana.com/0/481882893211075/1204303361994831/f
     * [DEDUPLICATION] -> Remote and Local data will be deduplicated and merged (Account Flows)
     * [REMOTE_WINS] -> Objects present in Remote will override objets in Local
     * [LOCAL_WINS] -> Object present in Local wins, Remote object is discarded
     * [TIMESTAMP] -> The last modified object wins, either from Remote or Local
     */
    enum class SyncConflictResolution {
        DEDUPLICATION,
        REMOTE_WINS,
        LOCAL_WINS,
        TIMESTAMP,
    }
}
