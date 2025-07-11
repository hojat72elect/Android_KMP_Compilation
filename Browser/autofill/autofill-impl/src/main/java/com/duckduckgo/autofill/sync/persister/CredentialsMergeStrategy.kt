

package com.duckduckgo.autofill.sync.persister

import androidx.annotation.WorkerThread
import com.duckduckgo.autofill.sync.credentialsSyncEntries
import com.duckduckgo.sync.api.engine.SyncMergeResult

interface CredentialsMergeStrategy {
    @WorkerThread
    fun processEntries(credentials: credentialsSyncEntries, clientModifiedSince: String): SyncMergeResult
}
