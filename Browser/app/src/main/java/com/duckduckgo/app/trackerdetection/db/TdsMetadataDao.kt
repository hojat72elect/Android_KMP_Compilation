

package com.duckduckgo.app.trackerdetection.db

import androidx.room.*
import com.duckduckgo.app.trackerdetection.model.TdsMetadata

@Dao
abstract class TdsMetadataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(tdsMetadata: TdsMetadata)

    @Query("SELECT eTag FROM tdsMetadata limit 1")
    abstract fun eTag(): String?

    @Transaction
    open fun tdsDownloadSuccessful(tdsMetadata: TdsMetadata) {
        insert(tdsMetadata)
    }
}
