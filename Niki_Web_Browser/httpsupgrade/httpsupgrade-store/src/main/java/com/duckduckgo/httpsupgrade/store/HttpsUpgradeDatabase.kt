

package com.duckduckgo.httpsupgrade.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        HttpsBloomFilterSpec::class,
        HttpsFalsePositiveDomain::class,
    ],
)
abstract class HttpsUpgradeDatabase : RoomDatabase() {
    abstract fun httpsBloomFilterSpecDao(): HttpsBloomFilterSpecDao
    abstract fun httpsFalsePositivesDao(): HttpsFalsePositivesDao

    companion object {
        val ALL_MIGRATIONS: List<Migration>
            get() = emptyList()
    }
}
