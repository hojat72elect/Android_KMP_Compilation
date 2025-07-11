

package com.duckduckgo.privacy.config.store.features.amplinks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.AmpKeywordEntity
import com.duckduckgo.privacy.config.store.AmpLinkExceptionEntity
import com.duckduckgo.privacy.config.store.AmpLinkFormatEntity

@Dao
abstract class AmpLinksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllExceptions(domains: List<AmpLinkExceptionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllAmpLinkFormats(ampLinkFormats: List<AmpLinkFormatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllAmpKeywords(ampKeywords: List<AmpKeywordEntity>)

    @Transaction
    open fun updateAll(domains: List<AmpLinkExceptionEntity>, ampLinkFormats: List<AmpLinkFormatEntity>, ampKeywords: List<AmpKeywordEntity>) {
        deleteAllExceptions()
        insertAllExceptions(domains)

        deleteAllAmpLinkFormats()
        insertAllAmpLinkFormats(ampLinkFormats)

        deleteAllAmpKeywords()
        insertAllAmpKeywords(ampKeywords)
    }

    @Query("select * from amp_exceptions")
    abstract fun getAllExceptions(): List<AmpLinkExceptionEntity>

    @Query("select * from amp_link_formats")
    abstract fun getAllAmpLinkFormats(): List<AmpLinkFormatEntity>

    @Query("select * from amp_keywords")
    abstract fun getAllAmpKeywords(): List<AmpKeywordEntity>

    @Query("delete from amp_exceptions")
    abstract fun deleteAllExceptions()

    @Query("delete from amp_link_formats")
    abstract fun deleteAllAmpLinkFormats()

    @Query("delete from amp_keywords")
    abstract fun deleteAllAmpKeywords()
}
