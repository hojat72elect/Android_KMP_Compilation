package ca.hojat.smart.musicplayer.shared.data.models

import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ca.hojat.smart.musicplayer.shared.extensions.buildMediaItem
import ca.hojat.smart.musicplayer.shared.helpers.AlphanumericComparator
import ca.hojat.smart.musicplayer.shared.helpers.SORT_DESCENDING
import ca.hojat.smart.musicplayer.shared.extensions.sortSafely
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_ARTIST_TITLE
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_DATE_ADDED
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_TITLE

@Entity(tableName = "albums", indices = [(Index(value = ["id"], unique = true))])
data class Album(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "cover_art") val coverArt: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "track_cnt") var trackCnt: Int,
    @ColumnInfo(name = "artist_id") var artistId: Long,
    @ColumnInfo(name = "date_added") var dateAdded: Int,
) : ListItem() {
    companion object {
        fun getComparator(sorting: Int) = Comparator<Album> { first, second ->
            var result = when {
                sorting and PLAYER_SORT_BY_TITLE != 0 -> {
                    when {
                        first.title == MediaStore.UNKNOWN_STRING && second.title != MediaStore.UNKNOWN_STRING -> 1
                        first.title != MediaStore.UNKNOWN_STRING && second.title == MediaStore.UNKNOWN_STRING -> -1
                        else -> AlphanumericComparator().compare(first.title.lowercase(), second.title.lowercase())
                    }
                }

                sorting and PLAYER_SORT_BY_ARTIST_TITLE != 0 -> {
                    when {
                        first.artist == MediaStore.UNKNOWN_STRING && second.artist != MediaStore.UNKNOWN_STRING -> 1
                        first.artist != MediaStore.UNKNOWN_STRING && second.artist == MediaStore.UNKNOWN_STRING -> -1
                        else -> AlphanumericComparator().compare(first.artist.lowercase(), second.artist.lowercase())
                    }
                }

                sorting and PLAYER_SORT_BY_DATE_ADDED != 0 -> first.dateAdded.compareTo(second.dateAdded)
                else -> first.year.compareTo(second.year)
            }

            if (sorting and SORT_DESCENDING != 0) {
                result *= -1
            }

            return@Comparator result
        }
    }

    fun getBubbleText(sorting: Int) = when {
        sorting and PLAYER_SORT_BY_TITLE != 0 -> title
        sorting and PLAYER_SORT_BY_ARTIST_TITLE != 0 -> artist
        else -> year.toString()
    }

    fun toMediaItem(): MediaItem {
        return buildMediaItem(
            mediaId = id.toString(),
            title = title,
            artist = artist,
            mediaType = MediaMetadata.MEDIA_TYPE_ALBUM,
            trackCnt = trackCnt,
            artworkUri = coverArt.toUri(),
            year = year
        )
    }
}

fun ArrayList<Album>.sortSafely(sorting: Int) = sortSafely(Album.getComparator(sorting))
