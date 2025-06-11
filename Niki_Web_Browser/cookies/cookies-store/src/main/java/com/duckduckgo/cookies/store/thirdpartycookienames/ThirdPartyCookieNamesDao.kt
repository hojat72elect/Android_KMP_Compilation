

package com.duckduckgo.cookies.store.thirdpartycookienames

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.cookies.store.CookieNamesEntity

@Dao
abstract class ThirdPartyCookieNamesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCookieNames(cookieNames: List<CookieNamesEntity>)

    @Transaction
    open fun updateAllCookieNames(cookieNames: List<CookieNamesEntity>) {
        deleteCookieNames()
        insertCookieNames(cookieNames)
    }

    @Query("select * from third_party_cookie_names")
    abstract fun getCookieNames(): List<CookieNamesEntity>

    @Query("delete from third_party_cookie_names")
    abstract fun deleteCookieNames()
}
