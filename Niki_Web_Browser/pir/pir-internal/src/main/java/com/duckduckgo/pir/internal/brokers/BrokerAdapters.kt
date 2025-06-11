

package com.duckduckgo.pir.internal.brokers

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject

class StepsAsStringAdapter {
    private val adapter by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Map::class.java) }

    @FromJson
    fun fromJson(steps: List<Map<String, Any>>): List<String> {
        return steps.map { adapter.toJson(it) }
    }

    // ToJson: Convert List<String> to List<Map<String, Any>>
    @ToJson
    fun toJson(steps: List<String>): List<Map<String, Any>> {
        return steps.map { adapter.fromJson(it) as Map<String, Any> }
    }
}

internal class JSONObjectAdapter {

    @FromJson
    fun fromJson(reader: JsonReader): JSONObject? {
        return (reader.readJsonValue() as? Map<*, *>)?.let { data ->
            try {
                JSONObject(data)
            } catch (e: JSONException) {
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
