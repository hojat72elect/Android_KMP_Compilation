package de.nulide.findmydevice.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import de.nulide.findmydevice.R
import de.nulide.findmydevice.data.ALLOWLIST_FILENAME
import de.nulide.findmydevice.data.AllowlistRepository
import de.nulide.findmydevice.data.SETTINGS_FILENAME
import de.nulide.findmydevice.data.SettingsRepository
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


private const val TAG = "ImportExportUtil"


class SettingsImportExporter(
    private val context: Context,
) {
    companion object {
        fun filenameForExport(): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                return "fmd-settings-$date.zip"
            } else {
                return "fmd-settings.zip"
            }
        }
    }

    fun exportData(uri: Uri) {
        writeToUri(context, uri) { outputStream ->
            val zipOutputStream = ZipOutputStream(outputStream)

            var entry = ZipEntry(SETTINGS_FILENAME)
            zipOutputStream.putNextEntry(entry)
            var writer = zipOutputStream.writer()
            SettingsRepository.getInstance(context).writeAsJson(writer)
            writer.flush()
            zipOutputStream.closeEntry()

            entry = ZipEntry(ALLOWLIST_FILENAME)
            zipOutputStream.putNextEntry(entry)
            writer = zipOutputStream.writer()
            writeAsJson(writer, AllowlistRepository.getInstance(context).list)
            writer.flush()
            zipOutputStream.closeEntry()

            zipOutputStream.close()
        }
    }

    fun importData(uri: Uri) {
        readFromUri(context, uri) { inputStream ->
            val path = uri.path ?: throw Exception("Missing URI path")

            // Newer FMD versions export ZIP files
            if (path.endsWith(".zip")) {
                val zipInputStream = ZipInputStream(inputStream)
                // Skip unknown files and silently ignore missing files
                while (true) {
                    val entry = zipInputStream.nextEntry ?: break

                    if (entry.name == SETTINGS_FILENAME) {
                        SettingsRepository.getInstance(context).importFromStream(zipInputStream)
                    } else if (entry.name == ALLOWLIST_FILENAME) {
                        AllowlistRepository.getInstance(context).importFromStream(zipInputStream)
                    }
                }
            }
            // Support old exports
            else if (path.endsWith(".json")) {
                SettingsRepository.getInstance(context).importFromStream(inputStream)
            } else {
                throw Exception("Unsupported file format")
            }
        }
    }
}

fun writeAsJson(
    outputStreamWriter: OutputStreamWriter,
    src: Any,
) {
    val type = src.javaClass
    val writer = JsonWriter(outputStreamWriter)
    Gson().toJson(src, type, writer)
}

fun writeToUri(
    context: Context,
    uri: Uri,
    write: (OutputStream) -> Unit,
) {
    var outputStream: OutputStream? = null
    try {
        outputStream = context.contentResolver.openOutputStream(uri) ?: return
        write(outputStream)
        Toast.makeText(context, R.string.export_success, Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        context.log().e(TAG, "Export failed:\n${e.stackTraceToString()}")
        Toast.makeText(context, R.string.export_failed, Toast.LENGTH_SHORT).show()
    } finally {
        outputStream?.close()
    }
}

fun readFromUri(
    context: Context,
    uri: Uri,
    read: (InputStream) -> Unit,
) {
    var inputStream: InputStream? = null
    try {
        inputStream = context.contentResolver.openInputStream(uri) ?: return
        read(inputStream)
        Toast.makeText(
            context, context.getString(R.string.Settings_Import_Success), Toast.LENGTH_SHORT
        ).show()
    } catch (e: Exception) {
        context.log().e(TAG, "Import failed:\n${e.stackTraceToString()}")
        Toast.makeText(
            context,
            context.getString(R.string.Settings_Import_Failed),
            Toast.LENGTH_SHORT
        ).show()
    } finally {
        inputStream?.close()
    }
}
