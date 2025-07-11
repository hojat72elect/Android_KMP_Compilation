package ca.hojat.smart.musicplayer.shared.data.models

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
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_TITLE

@Entity("genres", indices = [(Index(value = ["id"], unique = true))])
data class Genre(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "track_cnt") var trackCnt: Int,
    @ColumnInfo(name = "album_art") var albumArt: String
) {
    companion object {
        fun getComparator(sorting: Int) = Comparator<Genre> { first, second ->
            var result = when {
                sorting and PLAYER_SORT_BY_TITLE != 0 -> AlphanumericComparator().compare(first.title.lowercase(), second.title.lowercase())
                else -> first.trackCnt.compareTo(second.trackCnt)
            }

            if (sorting and SORT_DESCENDING != 0) {
                result *= -1
            }

            return@Comparator result
        }
    }

    fun getBubbleText(sorting: Int) = when {
        sorting and PLAYER_SORT_BY_TITLE != 0 -> title
        else -> trackCnt.toString()
    }

    fun toMediaItem(): MediaItem {
        return buildMediaItem(
            title = title,
            mediaId = id.toString(),
            mediaType = MediaMetadata.MEDIA_TYPE_GENRE,
            trackCnt = trackCnt,
            artworkUri = albumArt.toUri()
        )
    }
}

fun ArrayList<Genre>.sortSafely(sorting: Int) = sortSafely(Genre.getComparator(sorting))
