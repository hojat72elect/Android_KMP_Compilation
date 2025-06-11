

package com.duckduckgo.networkprotection.store.remote_config

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType

@Database(
    exportSchema = true,
    version = 3,
    entities = [
        NetPConfigToggle::class,
        NetPEgressServer::class,
        SelectedEgressServerName::class,
    ],
)
@TypeConverters(InternalDatabaseConverters::class)
abstract class NetPInternalConfigDatabase : RoomDatabase() {

    abstract fun configTogglesDao(): NetPConfigTogglesDao
    abstract fun serversDao(): NetPServersDao

    companion object {
        fun create(context: Context): NetPInternalConfigDatabase {
            return Room.databaseBuilder(context, NetPInternalConfigDatabase::class.java, "netp_internal.db")
                .enableMultiInstanceInvalidation()
                .fallbackToDestructiveMigration()
                .addMigrations(*ALL_MIGRATIONS)
                .build()
        }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `netp_egress_servers` ADD COLUMN `countryCode` TEXT")
        database.execSQL("ALTER TABLE `netp_egress_servers` ADD COLUMN `city` TEXT")
    }
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_2_3)

object InternalDatabaseConverters {

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
