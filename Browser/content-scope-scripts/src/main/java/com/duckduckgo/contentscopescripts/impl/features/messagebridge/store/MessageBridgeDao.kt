package com.duckduckgo.contentscopescripts.impl.features.messagebridge.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class MessageBridgeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(messageBridgeEntity: MessageBridgeEntity)

    @Transaction
    open fun updateAll(
        messageBridgeEntity: MessageBridgeEntity,
    ) {
        delete()
        insert(messageBridgeEntity)
    }

    @Query("select * from message_bridge")
    abstract fun get(): MessageBridgeEntity?

    @Query("delete from message_bridge")
    abstract fun delete()
}
