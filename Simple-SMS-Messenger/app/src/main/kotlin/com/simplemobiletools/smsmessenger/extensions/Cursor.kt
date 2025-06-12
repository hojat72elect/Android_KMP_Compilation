package com.simplemobiletools.smsmessenger.extensions

import android.annotation.SuppressLint
import android.database.Cursor
import com.google.gson.JsonNull
import com.google.gson.JsonObject

fun Cursor.rowsToJson(): JsonObject {
    val obj = JsonObject()
    for (i in 0 until columnCount) {
        val key = getColumnName(i)

        when (getType(i)) {
            Cursor.FIELD_TYPE_INTEGER -> obj.addProperty(key, getLong(i))
            Cursor.FIELD_TYPE_FLOAT -> obj.addProperty(key, getFloat(i))
            Cursor.FIELD_TYPE_STRING -> obj.addProperty(key, getString(i))
            Cursor.FIELD_TYPE_NULL -> obj.add(key, JsonNull.INSTANCE)
        }
    }
    return obj
}

@SuppressLint("Range")
fun Cursor.getStringValue(key: String) = getString(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getStringValueOrNull(key: String) =
    if (isNull(getColumnIndex(key))) null else getString(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getIntValue(key: String) = getInt(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getIntValueOrNull(key: String) =
    if (isNull(getColumnIndex(key))) null else getInt(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getLongValue(key: String) = getLong(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getLongValueOrNull(key: String) =
    if (isNull(getColumnIndex(key))) null else getLong(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getBlobValue(key: String) = getBlob(getColumnIndex(key))
