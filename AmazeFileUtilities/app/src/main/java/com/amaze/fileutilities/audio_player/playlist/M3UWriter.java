

package com.amaze.fileutilities.audio_player.playlist;

import com.amaze.fileutilities.home_page.ui.files.MediaFileInfo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class M3UWriter {

  private static final String EXTENSION = "m3u";
  private static final String HEADER = "#EXTM3U";
  private static final String ENTRY = "#EXTINF:";
  private static final String DURATION_SEPARATOR = ",";

  public static File write(File dir, MediaFileInfo.Playlist playlist, List<MediaFileInfo> songs)
      throws IOException {
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(dir, playlist.getName().concat("." + EXTENSION));

      if (!songs.isEmpty()) {
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));

      bw.write(HEADER);
      for (MediaFileInfo song : songs) {
        if (song.getExtraInfo() != null && song.getExtraInfo().getAudioMetaData() != null) {
          MediaFileInfo.AudioMetaData metaData = song.getExtraInfo().getAudioMetaData();
          bw.newLine();
          bw.write(
              ENTRY
                  + metaData.getDuration()
                  + DURATION_SEPARATOR
                  + metaData.getArtistName()
                  + " - "
                  + song.getTitle());
          bw.newLine();
          bw.write(song.getPath());
        }
      }

      bw.close();
    }

    return file;
  }
}
