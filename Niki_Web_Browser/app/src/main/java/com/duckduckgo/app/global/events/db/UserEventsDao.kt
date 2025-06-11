

package com.duckduckgo.app.global.events.db

import androidx.room.*

@Dao
interface UserEventsDao {

    @Query("select * from user_events where id=:userEventKey")
    suspend fun getUserEvent(userEventKey: UserEventKey): UserEventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEventEntity: UserEventEntity)

    @Query("delete from user_events where id=:userEventKey")
    fun delete(userEventKey: UserEventKey)
}
