package ca.hojat.gamehub.core.domain.entities

class DomainException(
    val error: Error,
    cause: Throwable? = null
) : Exception(error.toString(), cause)
