

package com.duckduckgo.mobile.android.vpn.dao

import androidx.room.*

@Entity(tableName = "vpn_heartbeat")
data class HeartBeatEntity(
    @PrimaryKey val type: String,
    val timestamp: Long = System.currentTimeMillis(),
)

@Dao
interface VpnHeartBeatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(heartBeatEntity: HeartBeatEntity)

    @Transaction
    fun insertType(type: String): HeartBeatEntity {
        return HeartBeatEntity(type).also {
            insert(it)
        }
    }

    @Query("select * from vpn_heartbeat where type = :type limit 1")
    fun getHearBeat(type: String): HeartBeatEntity?

    @Query("select * from vpn_heartbeat")
    fun hearBeats(): List<HeartBeatEntity>
}
