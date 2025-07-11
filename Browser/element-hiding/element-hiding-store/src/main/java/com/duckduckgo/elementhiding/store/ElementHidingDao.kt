

package com.duckduckgo.elementhiding.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class ElementHidingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(elementHidingEntity: ElementHidingEntity)

    @Transaction
    open fun updateAll(
        elementHidingEntity: ElementHidingEntity,
    ) {
        delete()
        insert(elementHidingEntity)
    }

    @Query("select * from element_hiding")
    abstract fun get(): ElementHidingEntity?

    @Query("delete from element_hiding")
    abstract fun delete()
}
