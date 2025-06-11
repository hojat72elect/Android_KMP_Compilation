

package com.duckduckgo.privacy.config.store.features.unprotectedtemporary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.UnprotectedTemporaryEntity

@Dao
abstract class UnprotectedTemporaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<UnprotectedTemporaryEntity>)

    @Transaction
    open fun updateAll(domains: List<UnprotectedTemporaryEntity>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select * from unprotected_temporary where domain = :domain")
    abstract fun get(domain: String): UnprotectedTemporaryEntity

    @Query("select * from unprotected_temporary")
    abstract fun getAll(): List<UnprotectedTemporaryEntity>

    @Query("delete from unprotected_temporary")
    abstract fun deleteAll()
}
