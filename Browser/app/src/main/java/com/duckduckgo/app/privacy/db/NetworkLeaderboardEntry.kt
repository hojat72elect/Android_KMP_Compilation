

package com.duckduckgo.app.privacy.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "network_leaderboard",
)
data class NetworkLeaderboardEntry(
    @PrimaryKey val networkName: String,
    val count: Int,
)
