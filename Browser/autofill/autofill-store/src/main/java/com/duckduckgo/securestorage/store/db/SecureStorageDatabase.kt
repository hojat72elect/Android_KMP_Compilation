

package com.duckduckgo.securestorage.store.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    version = 5,
    entities = [
        WebsiteLoginCredentialsEntity::class,
        NeverSavedSiteEntity::class,
    ],
)
abstract class SecureStorageDatabase : RoomDatabase() {
    abstract fun websiteLoginCredentialsDao(): WebsiteLoginCredentialsDao
    abstract fun neverSavedSitesDao(): NeverSavedSitesDao
}

val ALL_MIGRATIONS = arrayOf(
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `website_login_credentials` ADD COLUMN `notes` TEXT")
            db.execSQL("ALTER TABLE `website_login_credentials` ADD COLUMN `domainTitle` TEXT")
            db.execSQL("ALTER TABLE `website_login_credentials` ADD COLUMN `lastUpdatedInMillis` INTEGER")
        }
    },
    object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("UPDATE `website_login_credentials` SET `notes` = null")
            db.execSQL("ALTER TABLE `website_login_credentials` ADD COLUMN `notesIv` TEXT")
            db.execSQL("ALTER TABLE `website_login_credentials` RENAME COLUMN `iv` to `passwordIv`")
        }
    },
    object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `never_saved_sites` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `domain` TEXT)")
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_never_saved_sites_domain` ON `never_saved_sites` (`domain`)")
        }
    },
    object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `website_login_credentials` ADD COLUMN `lastUsedInMillis` INTEGER")
        }
    },
)
