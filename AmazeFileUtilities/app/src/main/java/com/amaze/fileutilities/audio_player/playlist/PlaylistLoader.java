

package com.amaze.fileutilities.audio_player.playlist;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.PlaylistsColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.amaze.fileutilities.home_page.ui.files.MediaFileInfo;
import java.util.ArrayList;
import java.util.List;

public class PlaylistLoader {

    @NonNull
    public static List<MediaFileInfo.Playlist> getAllPlaylists(@NonNull final Context context) {
        return getAllPlaylists(makePlaylistCursor(context, null, null));
    }

    @NonNull
    public static List<MediaFileInfo.Playlist> getAllPlaylists(@Nullable final Cursor cursor) {
        List<MediaFileInfo.Playlist> playlists = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                playlists.add(getPlaylistFromCursorImpl(cursor));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return playlists;
    }

    @NonNull
    private static MediaFileInfo.Playlist getPlaylistFromCursorImpl(@NonNull final Cursor cursor) {
        final long id = cursor.getLong(0);
        final String name = cursor.getString(1);
        return new MediaFileInfo.Playlist(id, name);
    }

    @Nullable
    public static Cursor makePlaylistCursor(
            @NonNull final Context context, final String selection, final String[] values) {
        try {
            return context
                    .getContentResolver()
                    .query(
                            MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                            new String[]{
                                    /* 0 */
                                    BaseColumns._ID,
                                    /* 1 */
                                    PlaylistsColumns.NAME
                            },
                            selection,
                            values,
                            MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER);
        } catch (SecurityException e) {
            return null;
        }
    }
}
