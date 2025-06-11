

package com.duckduckgo.app.bookmarks.db

import androidx.room.*
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: FavoriteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(favorites: List<FavoriteEntity>)

    @Query("select * from favorites order by position")
    fun favorites(): Flow<List<FavoriteEntity>>

    @Query("select * from favorites order by position")
    fun favoritesSync(): List<FavoriteEntity>

    @Query("select count(1) > 0 from favorites")
    fun userHasFavorites(): Boolean

    @Query("select count(*) from favorites WHERE url LIKE :domain")
    fun favoritesCountByUrl(domain: String): Int

    @Query("select count(*) from favorites")
    fun favoritesCount(): Long

    @Delete
    fun delete(favorite: FavoriteEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(favoriteEntity: FavoriteEntity)

    @Query("select * from favorites")
    fun favoritesObservable(): Single<List<FavoriteEntity>>

    @Query("select position from favorites where position = ( select MAX(position) from favorites)")
    fun getLastPosition(): Int?

    @Query("select * from favorites where id = :id")
    fun favorite(id: Long): FavoriteEntity?

    @Query("select * from favorites where url = :url limit 1")
    fun favoriteByUrl(url: String): FavoriteEntity?

    @Query("delete from favorites")
    fun deleteAll()
}
