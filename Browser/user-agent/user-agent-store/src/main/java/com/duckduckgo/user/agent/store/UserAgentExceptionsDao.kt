

package com.duckduckgo.user.agent.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class UserAgentExceptionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<UserAgentExceptionEntity>)

    @Transaction
    open fun updateAll(domains: List<UserAgentExceptionEntity>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select * from user_agent_exceptions")
    abstract fun getAll(): List<UserAgentExceptionEntity>

    @Query("delete from user_agent_exceptions")
    abstract fun deleteAll()
}
