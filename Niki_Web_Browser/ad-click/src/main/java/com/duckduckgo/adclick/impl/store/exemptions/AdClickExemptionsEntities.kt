package com.duckduckgo.adclick.impl.store.exemptions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tab_exemptions")
data class AdClickTabExemptionEntity(
    @PrimaryKey val tabId: String,
    val hostTldPlusOne: String,
    val navigationExemptionDeadline: Long,
    val exemptionDeadline: Long,
    val adClickActivePixelFired: Boolean = false,
)
