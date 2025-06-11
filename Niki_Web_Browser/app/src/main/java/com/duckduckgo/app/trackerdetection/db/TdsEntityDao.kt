

package com.duckduckgo.app.trackerdetection.db

import androidx.room.*
import com.duckduckgo.app.trackerdetection.model.TdsEntity

@Dao
abstract class TdsEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(entities: List<TdsEntity>)

    @Query("select * from tds_entity")
    abstract fun getAll(): List<TdsEntity>

    @Query("select * from tds_entity where name=:name")
    abstract fun get(name: String): TdsEntity?

    @Query("delete from tds_entity")
    abstract fun deleteAll()

    @Query("select count(*) from tds_entity")
    abstract fun count(): Int

    @Transaction
    open fun updateAll(entities: List<TdsEntity>) {
        deleteAll()
        insertAll(entities)
    }
}
