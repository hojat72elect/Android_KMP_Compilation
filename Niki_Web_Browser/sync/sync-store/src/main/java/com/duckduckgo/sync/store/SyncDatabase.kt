

package com.duckduckgo.sync.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.duckduckgo.sync.store.dao.SyncApiErrorDao
import com.duckduckgo.sync.store.dao.SyncAttemptDao
import com.duckduckgo.sync.store.dao.SyncOperationErrorDao
import com.duckduckgo.sync.store.model.SyncApiError
import com.duckduckgo.sync.store.model.SyncApiErrorType
import com.duckduckgo.sync.store.model.SyncAttempt
import com.duckduckgo.sync.store.model.SyncAttemptState
import com.duckduckgo.sync.store.model.SyncOperationError
import com.duckduckgo.sync.store.model.SyncOperationErrorType

@Database(
    exportSchema = true,
    version = 3,
    entities = [
        SyncAttempt::class,
        SyncApiError::class,
        SyncOperationError::class,
    ],
)
@TypeConverters(SyncTypeConverters::class)
abstract class SyncDatabase : RoomDatabase() {

    abstract fun syncAttemptsDao(): SyncAttemptDao

    abstract fun syncApiErrorsDao(): SyncApiErrorDao

    abstract fun syncOperationErrorsDao(): SyncOperationErrorDao
}

object SyncTypeConverters {

    @TypeConverter
    fun toSyncState(state: String): SyncAttemptState {
        return try {
            SyncAttemptState.valueOf(state)
        } catch (t: Throwable) {
            SyncAttemptState.FAIL
        }
    }

    @TypeConverter
    fun fromSyncState(syncState: SyncAttemptState): String {
        return syncState.name
    }

    @TypeConverter
    fun toSyncApiErrorType(errorType: String): SyncApiErrorType {
        return SyncApiErrorType.valueOf(errorType)
    }

    @TypeConverter
    fun fromSyncApiErrorType(errorType: SyncApiErrorType): String {
        return errorType.name
    }

    @TypeConverter
    fun toSyncOperationErrorType(errorType: String): SyncOperationErrorType {
        return SyncOperationErrorType.valueOf(errorType)
    }

    @TypeConverter
    fun fromSyncOperationErrorType(errorType: SyncOperationErrorType): String {
        return errorType.name
    }
}
