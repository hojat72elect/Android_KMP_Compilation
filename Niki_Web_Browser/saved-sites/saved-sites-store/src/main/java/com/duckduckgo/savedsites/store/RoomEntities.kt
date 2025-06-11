

package com.duckduckgo.savedsites.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import java.util.UUID

@Entity(tableName = "entities", primaryKeys = ["entityId"])
data class Entity(
    var entityId: String = UUID.randomUUID().toString(),
    var title: String,
    var url: String?,
    var type: EntityType,
    var lastModified: String? = DatabaseDateFormatter.iso8601(),
    var deleted: Boolean = false,
)

enum class EntityType {
    BOOKMARK,
    FOLDER,
    ;
}

class EntityTypeConverter {

    @TypeConverter
    fun toEntityType(entityType: String): EntityType {
        return try {
            EntityType.valueOf(entityType)
        } catch (ex: IllegalArgumentException) {
            EntityType.BOOKMARK
        }
    }

    @TypeConverter
    fun fromEntityType(entityType: EntityType): String {
        return entityType.name
    }
}

@Entity(tableName = "relations")
data class Relation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var folderId: String = UUID.randomUUID().toString(),
    var entityId: String,
)
