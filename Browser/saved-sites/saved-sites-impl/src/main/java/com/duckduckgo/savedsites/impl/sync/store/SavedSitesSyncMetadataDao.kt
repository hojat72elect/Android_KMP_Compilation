

package com.duckduckgo.savedsites.impl.sync.store

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SavedSitesSyncMetadataDao {

    @Query("select * from saved_sites_sync_meta")
    fun all(): List<SavedSitesSyncMetadataEntity>

    @Query("select * from saved_sites_sync_meta where `folderId` = :folderId")
    fun get(folderId: String): SavedSitesSyncMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdate(entity: SavedSitesSyncMetadataEntity): Long

    @Transaction
    fun addOrUpdate(entities: List<SavedSitesSyncMetadataEntity>) {
        entities.forEach {
            addOrUpdate(it)
        }
    }

    @Transaction
    fun confirmAllChildrenRequests() {
        copyAllRequestsToResponse()
    }

    @Query("update saved_sites_sync_meta set childrenResponse = childrenRequest where childrenRequest is not null")
    fun copyAllRequestsToResponse()

    @Query("update saved_sites_sync_meta set childrenRequest = null where childrenRequest is not null")
    fun discardRequestMetadata()

    @Transaction
    fun addResponseMetadata(folders: List<SavedSitesSyncMetadataEntity>) {
        addOrUpdate(folders)
        confirmAllChildrenRequests()
    }

    @Query("Delete from saved_sites_sync_meta")
    fun removeAll()

    @Transaction
    fun deleteMetadata(folders: List<String>) {
        folders.forEach {
            remove(it)
        }
    }

    @Query("delete from saved_sites_sync_meta where `folderId` = :folderId")
    fun remove(folderId: String)
}

@Entity(tableName = "saved_sites_sync_meta")
data class SavedSitesSyncMetadataEntity(
    @PrimaryKey val folderId: String,
    var childrenResponse: String?, // JSON representation of list of children confirmed by the BE
    var childrenRequest: String?, // JSON representation of list of children sent to the BE
)
