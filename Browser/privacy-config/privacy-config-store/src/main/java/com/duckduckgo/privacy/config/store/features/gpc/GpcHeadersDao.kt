

package com.duckduckgo.privacy.config.store.features.gpc

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.GpcHeaderEnabledSiteEntity

@Dao
abstract class GpcHeadersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<GpcHeaderEnabledSiteEntity>)

    @Transaction
    open fun updateAll(domains: List<GpcHeaderEnabledSiteEntity>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select * from gpc_header_enabled_sites where domain = :domain")
    abstract fun get(domain: String): GpcHeaderEnabledSiteEntity

    @Query("select * from gpc_header_enabled_sites")
    abstract fun getAll(): List<GpcHeaderEnabledSiteEntity>

    @Query("delete from gpc_header_enabled_sites")
    abstract fun deleteAll()
}
