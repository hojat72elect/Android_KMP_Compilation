

package com.duckduckgo.site.permissions.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.duckduckgo.site.permissions.store.sitepermissions.SitePermissionsDao
import com.duckduckgo.site.permissions.store.sitepermissions.SitePermissionsEntity
import com.duckduckgo.site.permissions.store.sitepermissionsallowed.SitePermissionAllowedEntity
import com.duckduckgo.site.permissions.store.sitepermissionsallowed.SitePermissionsAllowedDao

@Database(
    exportSchema = true,
    version = 5,
    entities = [
        SitePermissionsEntity::class,
        SitePermissionAllowedEntity::class,
    ],
)

abstract class SitePermissionsDatabase : RoomDatabase() {
    abstract fun sitePermissionsDao(): SitePermissionsDao
    abstract fun sitePermissionsAllowedDao(): SitePermissionsAllowedDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'site_permissions' ADD COLUMN 'askDrmSetting' TEXT NOT NULL DEFAULT 'ASK_EVERY_TIME'")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'site_permissions' ADD COLUMN 'askLocationSetting' TEXT NOT NULL DEFAULT 'ASK_EVERY_TIME'")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS `drm_block_exceptions`")
    }
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_3_4, MIGRATION_4_5)
