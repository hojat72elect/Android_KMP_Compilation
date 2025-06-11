

package com.duckduckgo.httpsupgrade.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "https_false_positive_domain")
data class HttpsFalsePositiveDomain(
    @PrimaryKey var domain: String,
)
