

package com.duckduckgo.app.privacy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.duckduckgo.app.privacy.model.UserAllowListedDomain
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserAllowListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(domain: UserAllowListedDomain)

    fun insert(domain: String) {
        insert(UserAllowListedDomain(domain))
    }

    @Delete
    abstract fun delete(domain: UserAllowListedDomain)

    fun delete(domain: String) {
        delete(UserAllowListedDomain(domain))
    }

    @Query("select * from user_whitelist")
    abstract fun all(): LiveData<List<UserAllowListedDomain>>

    @Query("select domain from user_whitelist")
    abstract fun allDomainsFlow(): Flow<List<String>>

    @Query("select count(1) > 0 from user_whitelist where domain = :domain")
    abstract fun contains(domain: String): Boolean
}
