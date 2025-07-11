

package com.duckduckgo.experiments.impl.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity(tableName = "experiment_variants")
data class ExperimentVariantEntity(
    @PrimaryKey val key: String,
    val weight: Double?,
    val localeFilter: List<String> = emptyList(),
    val androidVersionFilter: List<String> = emptyList(),
)

class StringListConverter {

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Adapters.stringListAdapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Adapters.stringListAdapter.toJson(value)
    }
}

class Adapters {
    companion object {
        private val moshi = Moshi.Builder().build()
        private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
        val stringListAdapter: JsonAdapter<List<String>> = moshi.adapter(stringListType)
    }
}
