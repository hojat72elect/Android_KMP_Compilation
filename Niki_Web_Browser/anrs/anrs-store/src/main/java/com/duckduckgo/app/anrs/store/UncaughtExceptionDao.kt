

package com.duckduckgo.app.anrs.store

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UncaughtExceptionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun add(exceptionEntity: ExceptionEntity)

    @Query("SELECT * FROM uncaught_exception_entity order by timestamp")
    abstract fun all(): List<ExceptionEntity>

    @Query("SELECT * FROM uncaught_exception_entity order by timestamp")
    abstract fun allFlow(): Flow<List<ExceptionEntity>>

    @Delete
    abstract fun delete(exceptionEntity: ExceptionEntity)
}
