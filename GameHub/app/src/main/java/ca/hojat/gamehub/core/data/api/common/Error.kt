package ca.hojat.gamehub.core.data.api.common

sealed class Error {
    data class HttpError(val code: Int, val message: String) : Error()
    data class NetworkError(val throwable: Throwable) : Error()
    data class UnknownError(val throwable: Throwable) : Error()

    val isHttpError: Boolean
        get() = (this is HttpError)

    val isServerError: Boolean
        get() = ((this is HttpError) && (code >= HttpCodes.SERVER_ERROR))

    val isNetworkError: Boolean
        get() = (this is NetworkError)

    val isUnknownError: Boolean
        get() = (this is UnknownError)

    val httpErrorMessage: String
        get() = (if (this is HttpError) message else "")

    val networkErrorMessage: String
        get() = (if (this is NetworkError) (throwable.message ?: "") else "")

    val unknownErrorMessage: String
        get() = (if (this is UnknownError) (throwable.message ?: "") else "")
}
