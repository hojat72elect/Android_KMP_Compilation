

package com.duckduckgo.httpsupgrade.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "https_bloom_filter_spec")
data class HttpsBloomFilterSpec(
    @PrimaryKey val id: Int = 1,
    val bitCount: Int,
    val errorRate: Double,
    val totalEntries: Int,
    val sha256: String,
) {
    companion object {
        const val HTTPS_BINARY_FILE = "HTTPS_BINARY_FILE"
    }
}
