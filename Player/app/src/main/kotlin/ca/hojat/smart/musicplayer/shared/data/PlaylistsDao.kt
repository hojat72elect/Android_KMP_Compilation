package ca.hojat.smart.musicplayer.shared.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ca.hojat.smart.musicplayer.shared.data.models.Playlist

@Dao
interface PlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: Playlist): Long

    @Delete
    fun deletePlaylists(playlists: List<Playlist?>)

    @Query("SELECT * FROM playlists")
    fun getAll(): List<Playlist>

    @Query("SELECT * FROM playlists WHERE title = :title COLLATE NOCASE")
    fun getPlaylistWithTitle(title: String): Playlist?

    @Query("SELECT * FROM playlists WHERE id = :id")
    fun getPlaylistWithId(id: Int): Playlist?

    @Update
    fun update(playlist: Playlist)
}
