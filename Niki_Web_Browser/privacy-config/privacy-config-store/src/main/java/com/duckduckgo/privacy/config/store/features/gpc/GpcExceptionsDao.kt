

package com.duckduckgo.privacy.config.store.features.gpc

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.GpcExceptionEntity

@Dao
abstract class GpcExceptionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<GpcExceptionEntity>)

    @Transaction
    open fun updateAll(domains: List<GpcExceptionEntity>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select * from gpc_exceptions where domain = :domain")
    abstract fun get(domain: String): GpcExceptionEntity

    @Query("select * from gpc_exceptions")
    abstract fun getAll(): List<GpcExceptionEntity>

    @Query("delete from gpc_exceptions")
    abstract fun deleteAll()
}
