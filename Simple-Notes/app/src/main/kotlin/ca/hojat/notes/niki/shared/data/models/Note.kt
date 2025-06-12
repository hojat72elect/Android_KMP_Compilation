package ca.hojat.notes.niki.shared.data.models

import android.content.Context
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ca.hojat.notes.niki.shared.extensions.isBiometricIdAvailable
import ca.hojat.notes.niki.shared.helpers.PROTECTION_FINGERPRINT
import ca.hojat.notes.niki.shared.helpers.PROTECTION_NONE
import java.io.File
import kotlinx.serialization.Serializable

/**
 * Represents a note.
 *
 * @property value The content of the note. Could be plain text or [ChecklistItem]
 * @property type The type of the note. Should be one of the [NoteType] enum entries.
 */
@Serializable
@Entity(tableName = "notes", indices = [(Index(value = ["id"], unique = true))])
@TypeConverters(NoteTypeConverter::class)
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "value") var value: String,
    @ColumnInfo(name = "type") var type: NoteType,
    @ColumnInfo(name = "path") var path: String,
    @ColumnInfo(name = "protection_type") var protectionType: Int,
    @ColumnInfo(name = "protection_hash") var protectionHash: String
) {

    fun getNoteStoredValue(context: Context): String? {
        return if (path.isNotEmpty()) {
            try {
                if (path.startsWith("content://")) {
                    val inputStream = context.contentResolver.openInputStream(Uri.parse(path))
                    inputStream?.bufferedReader().use { it!!.readText() }
                } else {
                    File(path).readText()
                }
            } catch (e: Exception) {
                null
            }
        } else {
            value
        }
    }

    fun isLocked() = protectionType != PROTECTION_NONE

    fun shouldBeUnlocked(context: Context): Boolean {
        return protectionType == PROTECTION_FINGERPRINT && !context.isBiometricIdAvailable()
    }
}
