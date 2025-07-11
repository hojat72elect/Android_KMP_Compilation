

package com.duckduckgo.networkprotection.store.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "netp_geoswitching_location")
data class NetPGeoswitchingLocation(
    @PrimaryKey val countryCode: String,
    val countryName: String,
    val cities: List<String>,
)
