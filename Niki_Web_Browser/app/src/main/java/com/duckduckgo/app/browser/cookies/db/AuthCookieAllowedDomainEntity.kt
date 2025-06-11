

package com.duckduckgo.app.browser.cookies.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_cookies_allowed_domains")
data class AuthCookieAllowedDomainEntity(
    @PrimaryKey
    var domain: String,
)
