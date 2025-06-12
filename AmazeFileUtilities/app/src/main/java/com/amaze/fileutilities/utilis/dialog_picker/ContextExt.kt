package com.amaze.fileutilities.utilis.dialog_picker

import android.content.Context
import java.io.File

internal fun Context.getExternalFilesDir(): File? {
    return this.getExternalFilesDir(null)
}
