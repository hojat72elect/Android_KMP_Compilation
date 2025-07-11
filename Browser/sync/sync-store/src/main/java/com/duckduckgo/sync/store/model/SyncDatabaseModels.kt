

package com.duckduckgo.sync.store.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import java.time.LocalDate
import java.time.ZoneOffset

@Entity(
    tableName = "sync_attempts",
)
data class SyncAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: String = DatabaseDateFormatter.iso8601(),
    val state: SyncAttemptState,
    val meta: String = "",
) {
    fun yesterday(): Boolean {
        return DatabaseDateFormatter.iso8601Date(this.timestamp).isEqual(LocalDate.now(ZoneOffset.UTC).minusDays(1))
    }
}

enum class SyncAttemptState {
    IN_PROGRESS,
    SUCCESS,
    FAIL,
}

@Entity(
    tableName = "sync_api_errors",
)
data class SyncApiError(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val feature: String,
    val errorType: SyncApiErrorType,
    val count: Int,
    val date: String = "", // YYYY-MM-dd format
)

enum class SyncApiErrorType {
    OBJECT_LIMIT_EXCEEDED,
    REQUEST_SIZE_LIMIT_EXCEEDED,
    VALIDATION_ERROR,
    TOO_MANY_REQUESTS,
}

@Entity(
    tableName = "sync_operation_errors",
)
data class SyncOperationError(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val feature: String = GENERIC_FEATURE,
    val errorType: SyncOperationErrorType,
    val count: Int,
    val date: String = "", // YYYY-MM-dd format
)

const val GENERIC_FEATURE = "Unknown"

enum class SyncOperationErrorType {
    DATA_PROVIDER_ERROR,
    DATA_PERSISTER_ERROR,
    DATA_ENCRYPT,
    DATA_DECRYPT,
    TIMESTAMP_CONFLICT,
    ORPHANS_PRESENT,
}
