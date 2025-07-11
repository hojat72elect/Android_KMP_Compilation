

package com.duckduckgo.app.notification.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duckduckgo.app.notification.model.Notification

@Dao
abstract class NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(notification: Notification)

    @Query("select count(1) > 0 from notification where notificationId = :notificationId")
    abstract fun exists(notificationId: String): Boolean
}
