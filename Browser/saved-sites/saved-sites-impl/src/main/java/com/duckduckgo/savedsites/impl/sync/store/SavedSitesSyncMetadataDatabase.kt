

package com.duckduckgo.savedsites.impl.sync.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        SavedSitesSyncMetadataEntity::class,
    ],
)
@TypeConverters(ChildrenTypeConverter::class)
abstract class SavedSitesSyncMetadataDatabase : RoomDatabase() {
    abstract fun syncMetadataDao(): SavedSitesSyncMetadataDao
}

object ChildrenTypeConverter {

    private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
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

val ALL_MIGRATIONS = emptyArray<Migration>()
