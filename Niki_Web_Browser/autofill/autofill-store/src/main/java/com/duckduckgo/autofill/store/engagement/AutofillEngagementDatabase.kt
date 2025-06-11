

package com.duckduckgo.autofill.store.engagement

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import java.time.LocalDate

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        AutofillEngagementEntity::class,
    ],
)
abstract class AutofillEngagementDatabase : RoomDatabase() {
    abstract fun autofillEngagementDao(): AutofillEngagementDao

    companion object {
        val ALL_MIGRATIONS = emptyArray<Migration>()
    }
}

@Entity(tableName = "autofill_engagement")
data class AutofillEngagementEntity(
    @PrimaryKey val date: String,
    val autofilled: Boolean,
    val searched: Boolean,
) {
    fun isToday(): Boolean {
        return date == LocalDate.now().toString()
    }
}
