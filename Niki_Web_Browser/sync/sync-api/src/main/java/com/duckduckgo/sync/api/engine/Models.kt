

package com.duckduckgo.sync.api.engine

import com.duckduckgo.sync.api.engine.SyncableType.BOOKMARKS

sealed class ModifiedSince(open val value: String) {
    object FirstSync : ModifiedSince(value = "0")
    data class Timestamp(override val value: String) : ModifiedSince(value)
}

// TODO: https://app.asana.com/0/0/1204958251694095/f
data class SyncChangesRequest(
    val type: SyncableType,
    val jsonString: String,
    val modifiedSince: ModifiedSince,
) {

    fun isEmpty(): Boolean {
        return this.jsonString.length == 0
    }

    fun isFirstSync(): Boolean {
        return this.modifiedSince is ModifiedSince.FirstSync
    }

    companion object {
        fun empty(): SyncChangesRequest {
            return SyncChangesRequest(BOOKMARKS, "", ModifiedSince.FirstSync)
        }
    }
}

data class SyncChangesResponse(
    val type: SyncableType,
    val jsonString: String,
) {
    fun isEmpty(): Boolean {
        return this.jsonString.length == 0
    }

    companion object {
        fun empty(type: SyncableType): SyncChangesResponse {
            return SyncChangesResponse(type, "")
        }
    }
}

data class SyncErrorResponse(
    val type: SyncableType,
    val featureSyncError: FeatureSyncError,
)

enum class FeatureSyncError {
    COLLECTION_LIMIT_REACHED,
    INVALID_REQUEST,
}

enum class SyncableType(val field: String) {
    BOOKMARKS("bookmarks"),
    CREDENTIALS("credentials"),
    SETTINGS("settings"),
}

// TODO: document api, when is it expected each case? https://app.asana.com/0/0/1204958251694095/f
sealed class SyncMergeResult {

    data class Success(
        val orphans: Boolean = false,
        val timestampConflict: Boolean = false,
    ) : SyncMergeResult()

    data class Error(
        val code: Int = -1,
        val reason: String,
    ) : SyncMergeResult()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[orphans=$orphans]"
            is Error -> "Error[exception=$code, $reason]"
        }
    }
}

sealed class SyncDataValidationResult<out R> {

    data class Success<out T>(val data: T) : SyncDataValidationResult<T>()

    object NoChanges : SyncDataValidationResult<Nothing>()

    data class Error(
        val reason: String,
    ) : SyncDataValidationResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[reason= $reason]"
            is NoChanges -> "No Changes"
        }
    }
}
