package ca.hojat.smart.musicplayer.shared.extensions

import android.content.Context
import ca.hojat.smart.musicplayer.shared.data.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
