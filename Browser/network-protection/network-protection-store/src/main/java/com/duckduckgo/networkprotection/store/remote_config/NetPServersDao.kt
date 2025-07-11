

package com.duckduckgo.networkprotection.store.remote_config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NetPServersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(server: List<NetPEgressServer>)

    @Query("delete from netp_egress_servers")
    suspend fun clearAll()

    @Query("SELECT * from netp_egress_servers")
    suspend fun getServers(): List<NetPEgressServer>

    @Query("DELETE from netp_selected_egress_server_name")
    suspend fun clearSelectedServer()

    @Query("SELECT * from netp_selected_egress_server_name LIMIT 1")
    suspend fun getSelectedServer(): SelectedEgressServer?

    @Query("INSERT or REPLACE into netp_selected_egress_server_name (id, name) values (1, :name)")
    suspend fun selectServer(name: String)
}
