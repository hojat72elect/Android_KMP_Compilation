package ca.hojat.gamehub.core.data.database.games.entities

import kotlinx.serialization.Serializable

@Serializable
data class DbInvolvedCompany(
    val company: DbCompany,
    val isDeveloper: Boolean,
    val isPublisher: Boolean,
    val isPorter: Boolean,
    val isSupporting: Boolean,
)
