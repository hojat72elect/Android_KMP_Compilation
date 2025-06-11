

package com.duckduckgo.site.permissions.store.sitepermissionsallowed

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SitePermissionsAllowedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sitePermissionAllowed: SitePermissionAllowedEntity): Long

    @Query("select * from site_permission_allowed")
    fun getAllSitesPermissionsAllowedAsFlow(): Flow<List<SitePermissionAllowedEntity>>

    @Query("select * from site_permission_allowed where domain = :domain and tabId = :tabId and permissionAllowed = :permissionAllowed")
    suspend fun getSitePermissionAllowed(domain: String, tabId: String, permissionAllowed: String): SitePermissionAllowedEntity?

    @Delete
    fun delete(sitePermissionsEntity: SitePermissionAllowedEntity): Int

    @Query("delete from site_permission_allowed")
    fun deleteAll()

    @Query("delete from site_permission_allowed where domain = :domain")
    fun deleteAllowedSitesForDomain(domain: String): Int
}
