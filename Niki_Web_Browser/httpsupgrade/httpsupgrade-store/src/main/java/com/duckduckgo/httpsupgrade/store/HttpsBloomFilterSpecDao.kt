

package com.duckduckgo.httpsupgrade.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HttpsBloomFilterSpecDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(specification: HttpsBloomFilterSpec)

    @Query("select * from https_bloom_filter_spec limit 1")
    fun get(): HttpsBloomFilterSpec?
}
