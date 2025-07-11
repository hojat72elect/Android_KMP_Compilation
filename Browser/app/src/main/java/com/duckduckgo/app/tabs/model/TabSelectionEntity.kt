

package com.duckduckgo.app.tabs.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tab_selection",
    foreignKeys = [
        ForeignKey(
            entity = TabEntity::class,
            parentColumns = ["tabId"],
            childColumns = ["tabId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index("tabId"),
    ],
)
data class TabSelectionEntity(
    @PrimaryKey var id: Int = 1,
    var tabId: String?,
)
