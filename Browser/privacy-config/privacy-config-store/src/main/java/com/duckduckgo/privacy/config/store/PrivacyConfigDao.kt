

package com.duckduckgo.privacy.config.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class PrivacyConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(privacyConfig: PrivacyConfig)

    @Query("select * from privacy_config LIMIT 1")
    abstract fun get(): PrivacyConfig?

    @Query("delete from privacy_config")
    abstract fun delete()
}
