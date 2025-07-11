package ca.hojat.smart.musicplayer.shared.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ca.hojat.smart.musicplayer.shared.data.models.Album

@Dao
interface AlbumsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(album: Album): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(albums: List<Album>)

    @Query("SELECT * FROM albums")
    fun getAll(): List<Album>

    @Query("SELECT * FROM albums WHERE id = :id")
    fun getAlbumWithId(id: Long): Album?

    @Query("SELECT * FROM albums WHERE artist_id = :artistId")
    fun getArtistAlbums(artistId: Long): List<Album>

    @Query("DELETE FROM albums WHERE id = :id")
    fun deleteAlbum(id: Long)
}
