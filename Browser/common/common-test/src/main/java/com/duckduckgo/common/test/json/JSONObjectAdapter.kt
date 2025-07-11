

package com.duckduckgo.common.test.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject

class JSONObjectAdapter {

    @FromJson
    fun fromJson(reader: JsonReader): JSONObject? {
        // Here we're expecting the JSON object, it is processed as Map<String, Any> by Moshi
        return (reader.readJsonValue() as? Map<*, *>)?.let { data ->
            try {
                JSONObject(data)
            } catch (e: JSONException) {
                // Handle exception
                return null
            }
        }
    }

    @ToJson
    fun toJson(
        writer: JsonWriter,
        value: JSONObject?,
    ) {
        value?.let { writer.run { value(Buffer().writeUtf8(value.toString())) } }
    }
}
