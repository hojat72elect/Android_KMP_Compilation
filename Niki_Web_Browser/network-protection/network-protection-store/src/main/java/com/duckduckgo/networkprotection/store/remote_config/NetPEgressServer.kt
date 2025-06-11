

package com.duckduckgo.networkprotection.store.remote_config

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "netp_egress_servers")
data class NetPEgressServer(
    @PrimaryKey val name: String,
    val publicKey: String,
    val hostnames: List<String>,
    val ips: List<String>,
    val port: Long,
    val countryCode: String? = null,
    val city: String? = null,
)

@Entity(tableName = "netp_selected_egress_server_name")
data class SelectedEgressServerName(
    @PrimaryKey val id: Long = 1,
    val name: String,
)

data class SelectedEgressServer(
    @Embedded val egressServerName: SelectedEgressServerName,
    @Relation(
        parentColumn = "name",
        entityColumn = "name",
    )
    val egressServer: NetPEgressServer?,
)
