

package com.duckduckgo.remote.messaging.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class RemoteMessagingConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(privacyConfig: RemoteMessagingConfig)

    @Query("select * from remote_messaging LIMIT 1")
    abstract fun get(): RemoteMessagingConfig?

    @Query("update remote_messaging set invalidate = 1")
    abstract fun invalidate()
}
