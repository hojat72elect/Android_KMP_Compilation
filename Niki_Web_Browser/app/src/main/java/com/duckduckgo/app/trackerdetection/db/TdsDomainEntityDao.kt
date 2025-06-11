

package com.duckduckgo.app.trackerdetection.db

import androidx.room.*
import com.duckduckgo.app.trackerdetection.model.TdsDomainEntity

@Dao
abstract class TdsDomainEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(entities: List<TdsDomainEntity>)

    @Query("select * from tds_domain_entity")
    abstract fun getAll(): List<TdsDomainEntity>

    @Query("select * from tds_domain_entity where domain=:domain")
    abstract fun get(domain: String): TdsDomainEntity?

    @Query("delete from tds_domain_entity")
    abstract fun deleteAll()

    @Query("select count(*) from tds_domain_entity")
    abstract fun count(): Int

    @Transaction
    open fun updateAll(entities: List<TdsDomainEntity>) {
        deleteAll()
        insertAll(entities)
    }
}
