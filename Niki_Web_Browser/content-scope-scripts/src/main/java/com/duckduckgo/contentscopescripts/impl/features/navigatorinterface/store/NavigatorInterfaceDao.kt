package com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class NavigatorInterfaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(navigatorInterfaceEntity: NavigatorInterfaceEntity)

    @Transaction
    open fun updateAll(
        navigatorInterfaceEntity: NavigatorInterfaceEntity,
    ) {
        delete()
        insert(navigatorInterfaceEntity)
    }

    @Query("select * from navigator_interface")
    abstract fun get(): NavigatorInterfaceEntity?

    @Query("delete from navigator_interface")
    abstract fun delete()
}
