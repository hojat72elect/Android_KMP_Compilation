

package com.duckduckgo.voice.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class VoiceSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllManufacturers(manufacturers: List<ManufacturerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllLocales(locales: List<LocaleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMinVersion(minVersion: MinVersionEntity)

    @Transaction
    open fun updateAll(manufacturers: List<ManufacturerEntity>, locales: List<LocaleEntity>, minVersion: MinVersionEntity) {
        deleteAllManufacturers()
        deleteAllLocales()
        deleteMinVersion()
        insertAllManufacturers(manufacturers)
        insertAllLocales(locales)
        insertMinVersion(minVersion)
    }

    @Query("select * from voice_search_manufacturers")
    abstract fun getManufacturerExceptions(): List<ManufacturerEntity>

    @Query("select * from voice_search_locales")
    abstract fun getLocaleExceptions(): List<LocaleEntity>

    @Query("select * from voice_search_min_version limit 1")
    abstract fun getMinVersion(): MinVersionEntity?

    @Query("delete from voice_search_min_version")
    abstract fun deleteMinVersion()

    @Query("delete from voice_search_manufacturers")
    abstract fun deleteAllManufacturers()

    @Query("delete from voice_search_locales")
    abstract fun deleteAllLocales()
}
