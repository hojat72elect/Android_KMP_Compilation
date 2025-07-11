

package com.duckduckgo.privacy.config.store.features.https

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.HttpsExceptionEntity

@Dao
abstract class HttpsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<HttpsExceptionEntity>)

    @Transaction
    open fun updateAll(domains: List<HttpsExceptionEntity>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select * from https_exceptions where domain = :domain")
    abstract fun get(domain: String): HttpsExceptionEntity

    @Query("select * from https_exceptions")
    abstract fun getAll(): List<HttpsExceptionEntity>

    @Query("delete from https_exceptions")
    abstract fun deleteAll()
}
