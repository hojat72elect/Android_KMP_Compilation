package com.duckduckgo.adclick.impl.store.exemptions

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        AdClickTabExemptionEntity::class,
    ],
)
abstract class AdClickExemptionsDatabase : RoomDatabase() {
    abstract fun adClickExemptionsDao(): AdClickExemptionsDao
}
