

package com.duckduckgo.mobile.android.vpn.dao

import androidx.room.*

@Dao
interface VpnFeatureRemoverDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(removerState: VpnFeatureRemoverState): Long

    @Query("select * from vpn_feature_remover")
    fun getState(): VpnFeatureRemoverState?
}

@Entity(tableName = "vpn_feature_remover")
data class VpnFeatureRemoverState(
    @PrimaryKey val id: Long = 1,
    val isFeatureRemoved: Boolean,
)
