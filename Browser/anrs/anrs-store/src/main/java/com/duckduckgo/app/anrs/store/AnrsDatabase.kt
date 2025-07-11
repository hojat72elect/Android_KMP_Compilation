

package com.duckduckgo.app.anrs.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType

@Database(
    exportSchema = true,
    version = 3,
    entities = [AnrEntity::class],
)
@TypeConverters(AnrTypeConverter::class)
abstract class AnrsDatabase : RoomDatabase() {
    abstract fun arnDao(): AnrDao

    companion object {

        private val MIGRATION_1_TO_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `anr_entity` ADD COLUMN `webView` TEXT NOT NULL DEFAULT \"unknown\"")
            }
        }

        private val MIGRATION_2_TO_3: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `anr_entity` ADD COLUMN `customTab` TEXT NOT NULL DEFAULT \"false\"")
            }
        }

        val ALL_MIGRATIONS: List<Migration>
            get() = listOf(
                MIGRATION_1_TO_2,
                MIGRATION_2_TO_3,
            )
    }
}

object AnrTypeConverter {

    private val stringListType = newParameterizedType(List::class.java, String::class.java)
    private val stringListAdapter: JsonAdapter<List<String>> = Moshi.Builder().build().adapter(stringListType)

    @TypeConverter
    @JvmStatic
    fun toStringList(value: String): List<String> {
        return stringListAdapter.fromJson(value)!!
    }

    @TypeConverter
    @JvmStatic
    fun fromStringList(value: List<String>): String {
        return stringListAdapter.toJson(value)
    }
}
