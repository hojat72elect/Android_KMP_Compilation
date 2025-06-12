package de.nulide.findmydevice.data

import android.content.Context
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.JsonReader
import de.nulide.findmydevice.data.UncaughtExceptionHandler.Companion.CRASH_MSG_HEADER
import de.nulide.findmydevice.utils.SingletonHolder
import java.io.File
import java.io.FileReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


const val LOG_FILENAME = "logs.json"
const val MAX_LOG_ENTRIES = 1000
const val LOG_PRUNE_THRESHOLD = MAX_LOG_ENTRIES + 200

data class LogEntry(
    val level: String,
    val timeMillis: Long,
    val tag: String,
    val msg: String,
)

class LogModel : LinkedList<LogEntry>()


class LogRepository private constructor(private val context: Context) {

    companion object : SingletonHolder<LogRepository, Context>(::LogRepository) {

        val TAG = LogRepository::class.simpleName

        fun filenameForExport(): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                return "fmd-logs-$date.json"
            } else {
                return "fmd-logs.json"
            }
        }
    }

    private val gson = Gson()

    val list: LogModel
    private var dirty = false

    init {
        val file = File(context.filesDir, LOG_FILENAME)
        if (!file.exists()) {
            file.createNewFile()
        }
        val reader = JsonReader(FileReader(file))

        list = try {
            gson.fromJson(reader, LogModel::class.java) ?: LogModel()
        } catch (e: JsonSyntaxException) {
            // https://gitlab.com/Nulide/findmydevice/-/issues/271
            // Log the error to ADB.
            // We do NOT log the error to the new, empty LogModel created below, in order to avoid
            // running into loops (in case this stack trace is what causes the JsonSyntaxException).
            Log.e(TAG, e.stackTraceToString())
            dirty = true
            // Silently reset the log
            LogModel()
        }
    }

    private fun save() {
        if (!dirty) return
        val raw = synchronized(list) { gson.toJson(list) }
        val file = File(context.filesDir, LOG_FILENAME)
        file.writeText(raw)
    }

    fun add(new: LogEntry) {
        synchronized(list) { list.add(new) }
        dirty = true
        prune()
        save()
    }

    fun prune() {
        // Prune the log when it becomes too large.
        // When we prune, prune a bit more than the pruning threshold.
        // This avoids pruning the log with every new entry.
        val size = synchronized(list) { list.size }
        if (size < LOG_PRUNE_THRESHOLD) {
            return
        }
        synchronized(list) {
            while (list.size > MAX_LOG_ENTRIES) {
                list.removeFirst()
            }
            dirty = true
            save()
        }
    }

    fun clearLog() = synchronized(list) {
        list.clear()
        dirty = true
        save()
    }

    fun getLastCrashLog(): LogEntry? = synchronized(list) {
        for (e in list.reversed()) {
            if (e.msg.startsWith(CRASH_MSG_HEADER)) {
                return e
            }
        }
        return null
    }
}
