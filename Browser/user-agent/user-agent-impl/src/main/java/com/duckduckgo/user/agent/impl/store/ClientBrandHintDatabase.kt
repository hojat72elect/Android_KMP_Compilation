

package com.duckduckgo.user.agent.impl.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        ClientHintBrandDomainEntity::class,
    ],
)
abstract class ClientBrandHintDatabase : RoomDatabase() {

    abstract fun clientBrandHintDao(): ClientBrandHintDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
