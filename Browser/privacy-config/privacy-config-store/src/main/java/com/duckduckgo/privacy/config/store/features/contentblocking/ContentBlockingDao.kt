

package com.duckduckgo.privacy.config.store.features.contentblocking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.ContentBlockingExceptionEntity

@Dao
abstract class ContentBlockingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<ContentBlockingExceptionEntity>)

    @Transaction
    open fun updateAll(domains: List<ContentBlockingExceptionEntity>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select * from content_blocking_exceptions where domain = :domain")
    abstract fun get(domain: String): ContentBlockingExceptionEntity

    @Query("select * from content_blocking_exceptions")
    abstract fun getAll(): List<ContentBlockingExceptionEntity>

    @Query("delete from content_blocking_exceptions")
    abstract fun deleteAll()
}
