

package com.duckduckgo.request.filterer.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class RequestFiltererDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSettings(settingsEntity: SettingsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllRequestFiltererExceptions(exceptions: List<RequestFiltererExceptionEntity>)

    @Transaction
    open fun updateAll(exceptions: List<RequestFiltererExceptionEntity>, settingsEntity: SettingsEntity) {
        deleteSettings()
        deleteAllExceptions()
        insertAllRequestFiltererExceptions(exceptions)
        insertSettings(settingsEntity)
    }

    @Query("select * from settings_entity")
    abstract fun getSettings(): SettingsEntity?

    @Query("select * from request_filterer_exceptions")
    abstract fun getAllRequestFiltererExceptions(): List<RequestFiltererExceptionEntity>

    @Query("delete from settings_entity")
    abstract fun deleteSettings()

    @Query("delete from request_filterer_exceptions")
    abstract fun deleteAllExceptions()
}
