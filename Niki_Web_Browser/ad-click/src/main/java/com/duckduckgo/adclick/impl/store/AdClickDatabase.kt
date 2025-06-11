package com.duckduckgo.adclick.impl.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    exportSchema = true,
    version = 2,
    entities = [
        AdClickAttributionLinkFormatEntity::class,
        AdClickAttributionAllowlistEntity::class,
        AdClickAttributionExpirationEntity::class,
        AdClickAttributionDetectionEntity::class,
    ],
)
abstract class AdClickDatabase : RoomDatabase() {
    abstract fun adClickDao(): AdClickDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Drop column is not supported by SQLite, so the data must be moved manually.
                with(database) {
                    execSQL("CREATE TABLE `link_formats_bk` (`url` TEXT NOT NULL, `adDomainParameterName` TEXT NOT NULL, PRIMARY KEY(`url`))")
                    execSQL("INSERT INTO `link_formats_bk` SELECT `url`, `adDomainParameterName` FROM `link_formats`")
                    execSQL("DROP TABLE `link_formats`")
                    execSQL("ALTER TABLE `link_formats_bk` RENAME to `link_formats`")
                }
            }
        }
        val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2)
    }
}
