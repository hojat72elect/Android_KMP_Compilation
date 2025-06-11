package ca.hojat.gamehub.core.domain.entities

sealed class Error {

    sealed class ApiError : Error() {
        data class ClientError(val message: String) : ApiError()
        data class NetworkError(val message: String) : ApiError()
        object ServiceUnavailable : ApiError()
        data class Unknown(val message: String) : ApiError()
    }

    data class NotFound(val message: String) : Error()
    data class Unknown(val message: String) : Error()
}
