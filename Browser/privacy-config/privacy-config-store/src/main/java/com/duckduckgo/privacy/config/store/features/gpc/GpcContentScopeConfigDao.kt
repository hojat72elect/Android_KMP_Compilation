

package com.duckduckgo.privacy.config.store.features.gpc

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duckduckgo.privacy.config.store.GpcContentScopeConfigEntity

@Dao
abstract class GpcContentScopeConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(config: GpcContentScopeConfigEntity)

    @Query("SELECT * FROM gpc_content_scope_config")
    abstract fun getConfig(): GpcContentScopeConfigEntity?
}
