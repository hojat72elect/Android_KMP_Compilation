package ca.hojat.gamehub.core.data.api.common

interface ErrorMessageExtractor {
    fun extract(responseBody: String): String
}
