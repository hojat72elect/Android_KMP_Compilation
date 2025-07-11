package ca.hojat.notes.niki.shared.data.models

import androidx.room.TypeConverter

class NoteTypeConverter {
    @TypeConverter
    fun fromNoteType(noteType: NoteType): Int {
        return noteType.value
    }

    @TypeConverter
    fun toNoteType(value: Int): NoteType {
        return NoteType.fromValue(value)
    }
}
