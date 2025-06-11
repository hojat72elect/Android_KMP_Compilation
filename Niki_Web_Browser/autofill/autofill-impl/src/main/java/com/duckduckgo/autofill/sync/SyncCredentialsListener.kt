

package com.duckduckgo.autofill.sync

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SingleInstanceIn(AppScope::class)
class SyncCredentialsListener @Inject constructor(
    private val credentialsSyncMetadata: CredentialsSyncMetadata,
    private val dispatcherProvider: DispatcherProvider,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
) {
    // we don't want to notify sync immediately of deletions in case they are deleted and the user chooses to UNDO that immediately after
    private val delayedDeleteJobs = mutableMapOf<String, Job>()

    fun onCredentialAdded(id: Long) {
        val mapId = id.toMapId()
        val undoDeleteRequested = delayedDeleteJobs[mapId] != null
        if (undoDeleteRequested) {
            cancelAndDeleteJob(mapId)
        } else {
            credentialsSyncMetadata.onEntityChanged(id)
        }
    }

    fun onCredentialsAdded(ids: List<Long>) {
        val mapId = ids.toMapId()
        val undoDeleteRequested = delayedDeleteJobs[mapId] != null
        if (undoDeleteRequested) {
            cancelAndDeleteJob(mapId)
        } else {
            credentialsSyncMetadata.onEntitiesChanged(ids)
        }
    }

    fun onCredentialUpdated(id: Long) {
        credentialsSyncMetadata.onEntityChanged(id)
    }

    fun onCredentialRemoved(id: Long) {
        val mapId = id.toMapId()
        appCoroutineScope.launch(dispatcherProvider.io()) {
            delay(SYNC_CREDENTIALS_DELETE_DELAY)
            credentialsSyncMetadata.onEntityRemoved(id)
            delayedDeleteJobs.remove(mapId)
        }.also {
            delayedDeleteJobs[mapId] = it
        }
    }

    fun onCredentialRemoved(ids: List<Long>) {
        val mapId = ids.toMapId()
        appCoroutineScope.launch(dispatcherProvider.io()) {
            delay(SYNC_CREDENTIALS_DELETE_DELAY)
            credentialsSyncMetadata.onEntitiesRemoved(ids)
            delayedDeleteJobs.remove(mapId)
        }.also {
            delayedDeleteJobs[mapId] = it
        }
    }

    private fun cancelAndDeleteJob(mapId: String) {
        delayedDeleteJobs[mapId]?.cancel()
        delayedDeleteJobs.remove(mapId)
    }

    private fun Long.toMapId(): String = this.toString()
    private fun List<Long>.toMapId(): String = this.joinToString()

    companion object {
        const val SYNC_CREDENTIALS_DELETE_DELAY = 5000L
    }
}
