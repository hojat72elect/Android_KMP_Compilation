

package com.duckduckgo.webcompat.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WebCompatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(webCompatEntity: WebCompatEntity)

    @Transaction
    fun updateAll(
        webCompatEntity: WebCompatEntity,
    ) {
        delete()
        insert(webCompatEntity)
    }

    @Query("select * from web_compat")
    fun get(): WebCompatEntity?

    @Query("delete from web_compat")
    fun delete()
}
