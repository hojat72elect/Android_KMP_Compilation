

package com.duckduckgo.app.trackerdetection.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.duckduckgo.app.browser.Domain
import com.duckduckgo.app.di.JsonModule
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types

@Entity(tableName = "tds_tracker")
data class TdsTracker(
    @PrimaryKey val domain: Domain,
    val defaultAction: Action,
    val ownerName: String,
    val categories: List<String>,
    val rules: List<Rule>,
)

enum class Action {
    BLOCK,
    IGNORE,
    UNSUPPORTED,
}

class Rule(
    val rule: String,
    val action: Action?,
    val exceptions: RuleExceptions?,
    val surrogate: String?,
    val options: Options?,
)

class RuleExceptions(
    val domains: List<String>?,
    val types: List<String>?,
)

class Options(
    val domains: List<String>?,
    val types: List<String>?,
)

class ActionTypeConverter {

    @TypeConverter
    fun toAction(value: String): Action {
        return Action.valueOf(value)
    }

    @TypeConverter
    fun fromAction(value: Action): String {
        return value.name
    }
}

class RuleTypeConverter {

    @TypeConverter
    fun toRules(value: String): List<Rule> {
        return Adapters.ruleListAdapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromRules(value: List<Rule>): String {
        return Adapters.ruleListAdapter.toJson(value)
    }
}

class CategoriesTypeConverter {

    @TypeConverter
    fun toCategories(value: String): List<String> {
        return Adapters.stringListAdapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromCategories(value: List<String>): String {
        return Adapters.stringListAdapter.toJson(value)
    }
}

class Adapters {
    companion object {
        private val moshi = JsonModule.moshi()
        private val ruleListType = Types.newParameterizedType(List::class.java, Rule::class.java)
        private val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
        val ruleListAdapter: JsonAdapter<List<Rule>> = moshi.adapter(ruleListType)
        val stringListAdapter: JsonAdapter<List<String>> = moshi.adapter(stringListType)
    }
}
