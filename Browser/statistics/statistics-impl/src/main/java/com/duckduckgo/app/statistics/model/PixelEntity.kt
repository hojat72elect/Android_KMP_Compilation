

package com.duckduckgo.app.statistics.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity(tableName = "pixel_store")
data class PixelEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val pixelName: String,
    val atb: String,
    val additionalQueryParams: Map<String, String> = emptyMap(),
    val encodedQueryParams: Map<String, String> = emptyMap(),
)

class QueryParamsTypeConverter {
    @TypeConverter
    fun toQueryParams(value: String): Map<String, String> {
        return Adapters.queryParamsAdapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromQueryParams(value: Map<String, String>): String {
        return Adapters.queryParamsAdapter.toJson(value)
    }
}

class Adapters {
    companion object {
        private val moshi = Moshi.Builder().build()
        private val mapStringStringType =
            Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
        val queryParamsAdapter: JsonAdapter<Map<String, String>> =
            moshi.adapter(mapStringStringType)
    }
}
