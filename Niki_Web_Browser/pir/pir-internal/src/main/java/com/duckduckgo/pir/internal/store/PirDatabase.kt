

package com.duckduckgo.pir.internal.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.duckduckgo.pir.internal.store.db.Broker
import com.duckduckgo.pir.internal.store.db.BrokerDao
import com.duckduckgo.pir.internal.store.db.BrokerJsonDao
import com.duckduckgo.pir.internal.store.db.BrokerJsonEtag
import com.duckduckgo.pir.internal.store.db.BrokerOptOut
import com.duckduckgo.pir.internal.store.db.BrokerScan
import com.duckduckgo.pir.internal.store.db.BrokerSchedulingConfig
import com.duckduckgo.pir.internal.store.db.ExtractProfileResult
import com.duckduckgo.pir.internal.store.db.OptOutActionLog
import com.duckduckgo.pir.internal.store.db.OptOutCompletedBroker
import com.duckduckgo.pir.internal.store.db.OptOutResultsDao
import com.duckduckgo.pir.internal.store.db.PirBrokerScanLog
import com.duckduckgo.pir.internal.store.db.PirEventLog
import com.duckduckgo.pir.internal.store.db.ScanCompletedBroker
import com.duckduckgo.pir.internal.store.db.ScanErrorResult
import com.duckduckgo.pir.internal.store.db.ScanLogDao
import com.duckduckgo.pir.internal.store.db.ScanNavigateResult
import com.duckduckgo.pir.internal.store.db.ScanResultsDao
import com.duckduckgo.pir.internal.store.db.UserProfile
import com.duckduckgo.pir.internal.store.db.UserProfileDao
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Database(
    exportSchema = true,
    version = 3,
    entities = [
        BrokerJsonEtag::class,
        Broker::class,
        BrokerOptOut::class,
        BrokerScan::class,
        BrokerSchedulingConfig::class,
        ScanNavigateResult::class,
        ScanErrorResult::class,
        ExtractProfileResult::class,
        UserProfile::class,
        PirEventLog::class,
        PirBrokerScanLog::class,
        ScanCompletedBroker::class,
        OptOutCompletedBroker::class,
        OptOutActionLog::class,
    ],
)
@TypeConverters(PirDatabaseConverters::class)
abstract class PirDatabase : RoomDatabase() {
    abstract fun brokerJsonDao(): BrokerJsonDao
    abstract fun brokerDao(): BrokerDao
    abstract fun scanResultsDao(): ScanResultsDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun scanLogDao(): ScanLogDao
    abstract fun optOutResultsDao(): OptOutResultsDao

    companion object {
        val ALL_MIGRATIONS: List<Migration>
            get() = emptyList()
    }
}

object PirDatabaseConverters {

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
