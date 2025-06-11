

package com.duckduckgo.app.surrogates.store

import android.content.Context
import javax.inject.Inject

class ResourceSurrogateDataStore @Inject constructor(private val context: Context) {

    fun hasData(): Boolean = context.fileExists(FILENAME)

    fun loadData(): ByteArray =
        context.openFileInput(FILENAME).use { it.readBytes() }

    fun saveData(byteArray: ByteArray) {
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { it.write(byteArray) }
    }

    fun clearData() {
        context.deleteFile(FILENAME)
    }

    private fun Context.fileExists(filename: String): Boolean {
        val file = getFileStreamPath(filename)
        return file != null && file.exists()
    }

    companion object {
        private const val FILENAME = "surrogates.js"
    }
}
