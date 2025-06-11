

package com.duckduckgo.privacyprotectionspopup.impl.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        PopupDismissDomain::class,
    ],
)
@TypeConverters(Converters::class)
abstract class PrivacyProtectionsPopupDatabase : RoomDatabase() {
    abstract fun popupDismissDomainDao(): PopupDismissDomainsDao
}

class Converters {
    @TypeConverter
    fun epochMilliToInstant(millis: Long?): Instant? =
        millis?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun instantToEpochMilli(instant: Instant?): Long? =
        instant?.toEpochMilli()
}
