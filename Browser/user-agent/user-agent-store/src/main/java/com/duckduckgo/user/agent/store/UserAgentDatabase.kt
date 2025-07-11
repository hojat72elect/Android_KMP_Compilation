

package com.duckduckgo.user.agent.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        UserAgentExceptionEntity::class,
    ],
)
abstract class UserAgentDatabase : RoomDatabase() {
    abstract fun userAgentExceptionsDao(): UserAgentExceptionsDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
