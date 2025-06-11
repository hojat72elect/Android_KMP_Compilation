

package com.duckduckgo.httpsupgrade.store

import androidx.room.*

@Dao
abstract class HttpsFalsePositivesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<HttpsFalsePositiveDomain>)

    @Transaction
    open fun updateAll(domains: List<HttpsFalsePositiveDomain>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select count(1) > 0 from https_false_positive_domain where domain = :domain")
    abstract fun contains(domain: String): Boolean

    @Query("delete from https_false_positive_domain")
    abstract fun deleteAll()

    @Query("select count(1) from https_false_positive_domain")
    abstract fun count(): Int
}
