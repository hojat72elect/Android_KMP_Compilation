package ca.hojat.smart.musicplayer.shared.data.models

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ca.hojat.smart.musicplayer.shared.extensions.buildMediaItem
import ca.hojat.smart.musicplayer.shared.helpers.AlphanumericComparator
import ca.hojat.smart.musicplayer.shared.helpers.SORT_DESCENDING
import ca.hojat.smart.musicplayer.shared.extensions.sortSafely
import ca.hojat.smart.musicplayer.shared.helpers.PLAYER_SORT_BY_TITLE

data class Folder(val title: String, val trackCount: Int, val path: String) {
    companion object {
        fun getComparator(sorting: Int) = Comparator<Folder> { first, second ->
            var result = when {
                sorting and PLAYER_SORT_BY_TITLE != 0 -> AlphanumericComparator().compare(first.title.lowercase(), second.title.lowercase())
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
            mediaId = title,
            title = title,
            mediaType = MediaMetadata.MEDIA_TYPE_PLAYLIST,
            trackCnt = trackCount
        )
    }
}

fun ArrayList<Folder>.sortSafely(sorting: Int) = sortSafely(Folder.getComparator(sorting))
