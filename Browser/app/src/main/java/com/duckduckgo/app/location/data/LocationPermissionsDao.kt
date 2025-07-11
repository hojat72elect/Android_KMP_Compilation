

package com.duckduckgo.app.location.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationPermissionsDao {

    @Query("select * from locationPermissions")
    fun allPermissions(): List<LocationPermissionEntity>

    @Query("select * from locationPermissions")
    fun allPermissionsEntities(): LiveData<List<LocationPermissionEntity>>

    @Query("select * from locationPermissions")
    fun allPermissionsAsFlow(): Flow<List<LocationPermissionEntity>>

    @Query("select * from locationPermissions WHERE domain = :domain")
    fun getPermission(domain: String): LocationPermissionEntity?

    @Query("select count(*) from locationPermissions WHERE domain LIKE :domain")
    fun permissionEntitiesCountByDomain(domain: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationPermissionEntity: LocationPermissionEntity): Long

    @Delete
    fun delete(locationPermissionEntity: LocationPermissionEntity): Int
}
