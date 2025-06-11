

package com.duckduckgo.malicioussiteprotection.impl.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    exportSchema = true,
    entities = [RevisionEntity::class, HashPrefixEntity::class, FilterEntity::class],
    version = 3,
)
abstract class MaliciousSitesDatabase : RoomDatabase() {
    abstract fun maliciousSiteDao(): MaliciousSiteDao

    companion object {
        internal val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS `featureExceptions`")
            }
        }
        internal val ALL_MIGRATIONS = arrayOf(MIGRATION_2_3)
    }
}
