package com.simplemobiletools.smsmessenger.extensions

import android.content.Context
import com.simplemobiletools.smsmessenger.models.FileDirItem


fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
