

package com.duckduckgo.sync.settings.impl

import androidx.room.*
import kotlinx.coroutines.flow.*

@Dao
interface SettingsSyncMetadataDao {

    @Query("select * from settings_sync_meta where `key` = :key")
    fun get(key: String): SettingsSyncMetadataEntity?

    @Query("select * from settings_sync_meta")
    fun getAllObservable(): Flow<List<SettingsSyncMetadataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdate(settingsSyncMetadataEntity: SettingsSyncMetadataEntity): Long

    @Query("select * from settings_sync_meta where modified_at > :since")
    fun getChangesSince(since: String): List<SettingsSyncMetadataEntity>

    @Query("Delete from settings_sync_meta")
    fun removeAll()

    @Transaction
    fun initialize(keys: List<String>) {
        removeAll()
        keys.map { SettingsSyncMetadataEntity(key = it, deleted_at = null, modified_at = SyncDateProvider.now()) }
            .forEach { addOrUpdate(it) }
    }
}
