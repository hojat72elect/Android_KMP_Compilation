package com.duckduckgo.autoconsent.impl.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "autoconsent_disabled_cmps")
data class DisabledCmpsEntity(
    @PrimaryKey val name: String,
)
