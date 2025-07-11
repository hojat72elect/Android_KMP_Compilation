

package com.duckduckgo.pir.internal.store.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BrokerJsonDao {
    @Query("SELECT etag from pir_broker_json_etag where fileName = :fileName")
    fun getEtag(fileName: String): String

    @Query("SELECT * from pir_broker_json_etag where isActive = 1 ")
    fun getAllActiveBrokers(): List<BrokerJsonEtag>

    @Query("SELECT * from pir_broker_json_etag where isActive = 1 ")
    fun getAllBrokers(): List<BrokerJsonEtag>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBrokerJsonEtags(etags: List<BrokerJsonEtag>)

    @Query("DELETE from pir_broker_json_etag")
    fun deleteAll()

    @Transaction
    fun upsertAll(etags: List<BrokerJsonEtag>) {
        deleteAll()
        insertBrokerJsonEtags(etags)
    }
}
