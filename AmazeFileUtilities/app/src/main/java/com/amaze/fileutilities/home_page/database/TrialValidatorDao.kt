package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrialValidatorDao {

    @Query("SELECT * FROM trial")
    fun getAll(): List<Trial>

    @Query("SELECT * FROM trial WHERE subscription_status<>1001")
    fun getAllSubscribed(): List<Trial>

    @Query("SELECT * FROM trial WHERE device_id=:deviceId")
    fun findByDeviceId(deviceId: String): Trial?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trial: Trial)

    @Delete
    fun deleteAll(vararg trial: Trial)

    @Delete
    fun delete(trial: Trial)
}
