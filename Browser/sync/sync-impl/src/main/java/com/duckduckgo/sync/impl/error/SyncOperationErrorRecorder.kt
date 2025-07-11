

package com.duckduckgo.sync.impl.error

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.impl.pixels.SyncPixels
import com.duckduckgo.sync.store.model.SyncOperationErrorType
import com.duckduckgo.sync.store.model.SyncOperationErrorType.TIMESTAMP_CONFLICT
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import timber.log.Timber

interface SyncOperationErrorRecorder {

    fun record(
        errorType: SyncOperationErrorType,
    )

    fun record(
        feature: String,
        errorType: SyncOperationErrorType,
    )
}

@ContributesBinding(AppScope::class)
class RealSyncOperationErrorRecorder @Inject constructor(
    private val syncPixels: SyncPixels,
    private val repository: SyncOperationErrorRepository,
) : SyncOperationErrorRecorder {
    override fun record(
        errorType: SyncOperationErrorType,
    ) {
        Timber.d("Sync-Error: Recording Operation Error $errorType")
        repository.addError(errorType)
    }

    override fun record(
        feature: String,
        errorType: SyncOperationErrorType,
    ) {
        Timber.d("Sync-Error: Recording Operation Error $errorType for $feature")
        if (errorType == TIMESTAMP_CONFLICT) {
            syncPixels.fireTimestampConflictPixel(feature)
        }

        repository.addError(feature, errorType)
    }
}
