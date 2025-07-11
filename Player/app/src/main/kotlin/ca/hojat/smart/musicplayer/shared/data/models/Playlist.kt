package ca.hojat.smart.musicplayer.shared.data.models

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import ca.hojat.smart.musicplayer.shared.extensions.buildMediaItem
import ca.hojat.smart.musicplayer.shared.helpers.AlphanumericComparator
import ca.hojat.smart.musicplayer.shared.helpers.SORT_DESCENDING
import ca.hojat.smart.musicplayer.shared.extensions.sortSafely
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_TITLE

@Entity(tableName = "playlists", indices = [(Index(value = ["id"], unique = true))])
data class Playlist(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "title") var title: String,

    @Ignore var trackCount: Int = 0
) {
    constructor() : this(0, "", 0)

    companion object {
        fun getComparator(sorting: Int) = Comparator<Playlist> { first, second ->
            var result = when {
                sorting and PLAYER_SORT_BY_TITLE != 0 -> AlphanumericComparator().compare(
                    first.title.lowercase(),
                    second.title.lowercase()
                )

                else -> first.trackCount.compareTo(second.trackCount)
            }

            if (sorting and SORT_DESCENDING != 0) {
                result *= -1
            }

            return@Comparator result
        }
    }

    fun getBubbleText(sorting: Int) = when {
        sorting and PLAYER_SORT_BY_TITLE != 0 -> title
        else -> trackCount.toString()
    }

    fun toMediaItem(): MediaItem {
        return buildMediaItem(
            mediaId = id.toString(),
            title = title,
            mediaType = MediaMetadata.MEDIA_TYPE_PLAYLIST,
            trackCnt = trackCount
        )
    }
}

fun ArrayList<Playlist>.sortSafely(sorting: Int) = sortSafely(Playlist.getComparator(sorting))
