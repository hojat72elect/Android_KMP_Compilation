

package com.duckduckgo.pir.internal.scripts.models

/**
 * This profile represents the data we can get from the web UI / from the user
 */
data class ProfileQuery(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val suffix: String? = null,
    val city: String,
    val state: String,
    val street: String? = null,
    val zip: String? = null,
    val addresses: List<Address>,
    val birthYear: Int,
    val phone: String? = null,
    val fullName: String,
    val age: Int,
    val deprecated: Boolean,
)

data class Address(
    val city: String,
    val state: String,
)

data class ExtractedProfile(
    val id: Int? = null,
    val name: String? = null,
    val alternativeNamesList: List<String>? = emptyList(),
    val addressFull: List<AddressCityState>? = emptyList(),
    val addressCityState: List<AddressCityState>? = emptyList(),
    val phoneNumbers: List<String>? = null,
    val relativesList: List<String>? = null,
    val profileUrl: ProfileUrl? = null,
    val reportId: String? = null,
    val age: String? = null,
    val email: String? = null,
    val removedDate: String? = null,
    val fullName: String? = null,
    val identifier: String? = null,
)

data class ProfileUrl(
    val profileUrl: String,
    val identifier: String,
)

data class AddressCityState(
    val city: String,
    val state: String,
    val fullAddress: String? = null,
)
