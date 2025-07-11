

package com.duckduckgo.cookies.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class CookiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFirstPartyCookiePolicy(firstPartyTrackerCookiePolicy: FirstPartyCookiePolicyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllCookieExceptions(exceptions: List<CookieExceptionEntity>)

    @Transaction
    open fun updateAll(exceptions: List<CookieExceptionEntity>, firstPartyTrackerCookiePolicy: FirstPartyCookiePolicyEntity) {
        deleteFirstPartyCookiePolicy()
        deleteAllExceptions()
        insertAllCookieExceptions(exceptions)
        insertFirstPartyCookiePolicy(firstPartyTrackerCookiePolicy)
    }

    @Query("select * from first_party_cookie_policy")
    abstract fun getFirstPartyCookiePolicy(): FirstPartyCookiePolicyEntity?

    @Query("select * from cookie_exceptions")
    abstract fun getAllCookieExceptions(): List<CookieExceptionEntity>

    @Query("delete from first_party_cookie_policy")
    abstract fun deleteFirstPartyCookiePolicy()

    @Query("delete from cookie_exceptions")
    abstract fun deleteAllExceptions()
}
