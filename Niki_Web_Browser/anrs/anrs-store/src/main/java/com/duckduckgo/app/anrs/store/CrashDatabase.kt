

package com.duckduckgo.app.anrs.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    exportSchema = true,
    version = 3,
    entities = [ExceptionEntity::class],
)
abstract class CrashDatabase : RoomDatabase() {
    abstract fun uncaughtExceptionDao(): UncaughtExceptionDao

    companion object {

        private val MIGRATION_1_TO_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `uncaught_exception_entity` ADD COLUMN `webView` TEXT NOT NULL DEFAULT \"unknown\"")
            }
        }

        private val MIGRATION_2_TO_3: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `uncaught_exception_entity` ADD COLUMN `customTab` TEXT NOT NULL DEFAULT \"false\"")
            }
        }

        val ALL_MIGRATIONS: List<Migration>
            get() = listOf(
                MIGRATION_1_TO_2,
                MIGRATION_2_TO_3,
            )
    }
}
