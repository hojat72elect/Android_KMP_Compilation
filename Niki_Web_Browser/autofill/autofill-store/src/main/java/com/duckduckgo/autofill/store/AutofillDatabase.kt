

package com.duckduckgo.autofill.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    exportSchema = true,
    version = 3,
    entities = [
        CredentialsSyncMetadataEntity::class,
    ],
)
abstract class AutofillDatabase : RoomDatabase() {
    abstract fun credentialsSyncDao(): CredentialsSyncMetadataDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `credentials_sync_meta` " +
                "(`syncId` TEXT NOT NULL, `localId` INTEGER NOT NULL, `deleted_at` TEXT, `modified_at` TEXT, PRIMARY KEY(`localId`))",
        )
        database.execSQL(
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_credentials_sync_meta_syncId` ON `credentials_sync_meta` (`syncId`)",
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE `autofill_exceptions`")
    }
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_2_3)
