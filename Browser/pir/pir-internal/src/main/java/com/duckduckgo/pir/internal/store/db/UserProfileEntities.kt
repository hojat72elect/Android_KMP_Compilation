

package com.duckduckgo.pir.internal.store.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pir_user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @Embedded(prefix = "user_")
    val userName: UserName,
    @Embedded(prefix = "address_")
    val addresses: Address,
    val birthYear: Int,
    val phone: String? = null,
)

data class UserName(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val suffix: String? = null,
)

data class Address(
    val city: String,
    val state: String,
    val street: String? = null,
    val zip: String? = null,
)
