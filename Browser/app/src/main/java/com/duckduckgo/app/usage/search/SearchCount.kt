

package com.duckduckgo.app.usage.search

import androidx.room.*
import com.duckduckgo.di.scopes.AppScope
import dagger.SingleInstanceIn

@Dao
@SingleInstanceIn(AppScope::class)
abstract class SearchCountDao {

    @Query("SELECT count FROM search_count LIMIT 1")
    abstract fun getSearchesMade(): Long

    @Query("UPDATE search_count SET count = count + 1")
    abstract fun incrementSearchCountIfExists(): Int

    @Insert
    abstract fun initialiseValue(entity: SearchCountEntity)

    /**
     * We need to increment the value in the DB, but the row might not exist yet.
     * We therefore update if the row exists, or insert a new value if it doesn't.
     */
    @Transaction
    open fun incrementSearchCount() {
        val changedRows = incrementSearchCountIfExists()
        if (changedRows == 0) {
            initialiseValue(SearchCountEntity(count = 1))
        }
    }
}

@Entity(tableName = "search_count")
data class SearchCountEntity(
    @PrimaryKey val key: String = SINGLETON_KEY,
    val count: Long,
) {

    companion object {
        const val SINGLETON_KEY = "SINGLETON_KEY"
    }
}
