package com.amaze.fileutilities.utilis

import androidx.room.TypeConverter
import java.util.Date
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DbConverters {


    @TypeConverter
    fun fromList(value: List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<String>>(value)

    @TypeConverter
    fun fromSet(value: Set<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toSet(value: String) = Json.decodeFromString<Set<String>>(value)

    @TypeConverter
    fun fromIntPairList(value: List<Pair<Int, Int>>) = Json.encodeToString(value)


    @TypeConverter
    fun toIntPairList(value: String) = Json.decodeFromString<List<Pair<Int, Int>>>(value)

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
