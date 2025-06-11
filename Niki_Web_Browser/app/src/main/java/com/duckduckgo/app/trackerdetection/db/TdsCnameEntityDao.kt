

package com.duckduckgo.app.trackerdetection.db

import androidx.room.*
import com.duckduckgo.app.trackerdetection.model.TdsCnameEntity

@Dao
abstract class TdsCnameEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(entities: List<TdsCnameEntity>)

    @Query("select * from tds_cname_entity")
    abstract fun getAll(): List<TdsCnameEntity>

    @Query("select * from tds_cname_entity where cloakedHostName=:cloakedHostName")
    abstract fun get(cloakedHostName: String): TdsCnameEntity?

    @Query("delete from tds_cname_entity")
    abstract fun deleteAll()

    @Query("select count(*) from tds_cname_entity")
    abstract fun count(): Int

    @Transaction
    open fun updateAll(entities: List<TdsCnameEntity>) {
        deleteAll()
        insertAll(entities)
    }
}
