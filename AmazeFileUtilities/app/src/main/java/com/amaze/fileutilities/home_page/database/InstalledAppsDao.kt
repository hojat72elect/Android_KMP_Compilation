package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface InstalledAppsDao {

    @Query("SELECT * FROM installedapps WHERE package_name=:packageName")
    fun findByPackageName(packageName: String): InstalledApps?

    @Query("SELECT * FROM installedapps")
    fun findAll(): List<InstalledApps>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(installedApp: InstalledApps): Long

    @Update
    fun update(installedApp: InstalledApps)

    @Transaction
    fun updateOrInsert(installedAppsList: List<InstalledApps>) {
        for (installedApp in installedAppsList) {
            val rowId = insert(installedApp)
            if (rowId == -1L) {
                // there is already an entry so we want to update
                update(installedApp)
            }
        }
    }

    @Query("DELETE FROM installedapps")
    fun deleteAll()
}
