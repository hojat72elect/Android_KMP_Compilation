

package com.duckduckgo.app.privacy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_whitelist")
data class UserAllowListedDomain(
    @PrimaryKey val domain: String,
)
