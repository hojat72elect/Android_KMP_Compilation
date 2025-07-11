

package com.duckduckgo.autofill.sync.persister

import com.duckduckgo.autofill.api.domain.app.*
import com.duckduckgo.autofill.sync.*
import com.duckduckgo.autofill.sync.isDeleted
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.sync.api.engine.SyncMergeResult
import com.duckduckgo.sync.api.engine.SyncMergeResult.Error
import com.duckduckgo.sync.api.engine.SyncMergeResult.Success
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class CredentialsRemoteWinsStrategy constructor(
    private val credentialsSync: CredentialsSync,
    private val credentialsSyncMapper: CredentialsSyncMapper,
    private val dispatchers: DispatcherProvider,
) : CredentialsMergeStrategy {
    override fun processEntries(
        credentials: credentialsSyncEntries,
        clientModifiedSince: String,
    ): SyncMergeResult {
        Timber.d("Sync-autofill-Persist: ======= MERGING REMOTEWINS =======")
        return kotlin.runCatching {
            runBlocking(dispatchers.io()) {
                credentials.entries.forEach { entry ->
                    val localCredential = credentialsSync.getCredentialWithSyncId(entry.id)
                    if (localCredential != null) {
                        processExistingEntry(localCredential, entry, credentials.last_modified)
                    } else {
                        processNewEntry(entry, credentials.last_modified)
                    }
                }
            }
        }.getOrElse {
            Timber.d("Sync-autofill-Persist: merging failed with error $it")
            return Error(reason = "RemoteWins merge failed with error $it")
        }.let {
            Timber.d("Sync-autofill-Persist: merging completed")
            Success()
        }
    }

    private suspend fun processNewEntry(remoteEntry: CredentialsSyncEntryResponse, clientModifiedSince: String) {
        if (remoteEntry.isDeleted()) return
        val updatedCredentials = credentialsSyncMapper.toLoginCredential(
            remoteEntry = remoteEntry,
            lastModified = clientModifiedSince,
        )
        Timber.d("Sync-autofill-Persist: >>> save remote $updatedCredentials")
        credentialsSync.saveCredential(updatedCredentials, remoteEntry.id)
    }

    private suspend fun processExistingEntry(
        localCredential: LoginCredentials,
        remoteEntry: CredentialsSyncEntryResponse,
        clientModifiedSince: String,
    ) {
        val localId = localCredential.id!!
        if (remoteEntry.isDeleted()) {
            Timber.d("Sync-autofill-Persist: >>> delete local $localId")
            credentialsSync.deleteCredential(localId)
            return
        }
        val updatedCredentials = credentialsSyncMapper.toLoginCredential(remoteEntry, localId, clientModifiedSince)
        credentialsSync.updateCredentials(updatedCredentials, remoteEntry.id)
    }
}
