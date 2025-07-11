

package com.duckduckgo.remote.messaging.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    exportSchema = true,
    version = 2,
    entities = [
        RemoteMessagingConfig::class,
        RemoteMessageEntity::class,
        RemoteMessagingCohort::class,
    ],
)
abstract class RemoteMessagingDatabase : RoomDatabase() {
    abstract fun remoteMessagingConfigDao(): RemoteMessagingConfigDao
    abstract fun remoteMessagesDao(): RemoteMessagesDao
    abstract fun remoteMessagingCohortDao(): RemoteMessagingCohortDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                with(database) {
                    execSQL(
                        "CREATE TABLE IF NOT EXISTS `remote_messaging_cohort` " +
                            "(`messageId` TEXT NOT NULL," +
                            " `percentile` REAL NOT NULL," +
                            " PRIMARY KEY(`messageId`))",
                    )
                }
            }
        }
        val ALL_MIGRATIONS: Array<Migration>
            get() = arrayOf(MIGRATION_1_2)
    }
}
