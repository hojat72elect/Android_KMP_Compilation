

package com.duckduckgo.cookies.store.contentscopescripts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.cookies.store.CookieEntity

@Dao
interface ContentScopeScriptsCookieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cookieEntity: CookieEntity)

    @Transaction
    fun updateAll(
        cookieEntity: CookieEntity,
    ) {
        delete()
        insert(cookieEntity)
    }

    @Query("select * from cookie")
    fun get(): CookieEntity?

    @Query("delete from cookie")
    fun delete()
}
