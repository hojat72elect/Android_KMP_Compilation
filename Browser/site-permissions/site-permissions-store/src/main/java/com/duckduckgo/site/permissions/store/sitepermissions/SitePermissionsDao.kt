

package com.duckduckgo.site.permissions.store.sitepermissions

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SitePermissionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sitePermissionsEntity: SitePermissionsEntity): Long

    @Query("select * from site_permissions")
    fun getAllSitesPermissions(): List<SitePermissionsEntity>

    @Query("select * from site_permissions")
    fun getAllSitesPermissionsAsFlow(): Flow<List<SitePermissionsEntity>>

    @Query("select * from site_permissions where domain = :domain")
    suspend fun getSitePermissionsByDomain(domain: String): SitePermissionsEntity?

    @Delete
    fun delete(sitePermissionsEntity: SitePermissionsEntity): Int

    @Query("delete from site_permissions")
    fun deleteAll()
}
