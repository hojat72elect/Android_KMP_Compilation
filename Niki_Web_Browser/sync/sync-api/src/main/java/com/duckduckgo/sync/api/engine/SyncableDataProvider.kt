

package com.duckduckgo.sync.api.engine

interface SyncableDataProvider {
    fun getType(): SyncableType

    /**
     * Used by the SyncClient to get all the updates from each syncable feature
     * since a specific time
     * This data that will be sent to the Sync API
     */
    fun getChanges(): SyncChangesRequest
}
