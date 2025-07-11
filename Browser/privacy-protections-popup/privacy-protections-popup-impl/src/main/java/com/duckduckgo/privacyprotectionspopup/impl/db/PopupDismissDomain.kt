

package com.duckduckgo.privacyprotectionspopup.impl.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "popup_dismiss_domains")
data class PopupDismissDomain(
    @PrimaryKey val domain: String,
    val dismissedAt: Instant,
)
