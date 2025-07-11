package com.duckduckgo.autoconsent.impl.store

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    exportSchema = true,
    version = 2,
    entities = [
        DisabledCmpsEntity::class,
    ],
)
abstract class AutoconsentDatabase : RoomDatabase() {
    abstract fun autoconsentDao(): AutoconsentDao
}
