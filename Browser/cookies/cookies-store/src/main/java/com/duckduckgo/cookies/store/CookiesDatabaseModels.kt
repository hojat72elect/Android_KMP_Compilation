// ktlint-disable filename

package com.duckduckgo.cookies.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.feature.toggles.api.FeatureException

@Entity(tableName = "first_party_cookie_policy")
data class FirstPartyCookiePolicyEntity(
    @PrimaryKey val id: Int = 1,
    val threshold: Int,
    val maxAge: Int,
)

@Entity(tableName = "cookie_exceptions")
data class CookieExceptionEntity(
    @PrimaryKey val domain: String,
    val reason: String,
)

@Entity(tableName = "cookie")
data class CookieEntity(
    @PrimaryKey val id: Int = 1,
    val json: String,
)

@Entity(tableName = "third_party_cookie_names")
data class CookieNamesEntity(
    @PrimaryKey val name: String,
)

fun CookieExceptionEntity.toFeatureException(): FeatureException {
    return FeatureException(domain = this.domain, reason = this.reason)
}
