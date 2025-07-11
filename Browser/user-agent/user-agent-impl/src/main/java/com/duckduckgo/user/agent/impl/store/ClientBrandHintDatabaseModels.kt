

package com.duckduckgo.user.agent.impl.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_agent_client_hint_brand_domains")
data class ClientHintBrandDomainEntity(
    @PrimaryKey
    val url: String,
    val brand: String,
)
