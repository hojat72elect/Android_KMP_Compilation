

package com.duckduckgo.user.agent.impl.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class ClientBrandHintDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDomains(exceptions: List<ClientHintBrandDomainEntity>)

    @Query("select * from user_agent_client_hint_brand_domains")
    abstract fun getAllDomains(): List<ClientHintBrandDomainEntity>

    @Transaction
    open fun updateAllDomains(domains: List<ClientHintBrandDomainEntity>) {
        deleteDomains()
        insertDomains(domains)
    }

    @Query("delete from user_agent_client_hint_brand_domains")
    abstract fun deleteDomains()
}
