

package com.duckduckgo.app.browser.cookies.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthCookiesAllowedDomainsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authCookieAllowedDomainEntity: AuthCookieAllowedDomainEntity): Long

    @Delete
    fun delete(authCookieAllowedDomainEntity: AuthCookieAllowedDomainEntity)

    @Query("SELECT * FROM auth_cookies_allowed_domains WHERE domain = :host limit 1")
    fun getDomain(host: String): AuthCookieAllowedDomainEntity?

    @Query("DELETE FROM auth_cookies_allowed_domains WHERE domain NOT IN (:exceptionList)")
    fun deleteAll(exceptionList: String)
}
