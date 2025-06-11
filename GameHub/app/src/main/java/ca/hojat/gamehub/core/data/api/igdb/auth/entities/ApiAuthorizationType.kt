package ca.hojat.gamehub.core.data.api.igdb.auth.entities

enum class ApiAuthorizationType(val rawType: String) {
    BASIC("Basic"),
    BEARER("Bearer");

    companion object {

        fun forRawType(rawType: String): ApiAuthorizationType {
            return values().find { it.rawType == rawType }
                ?: throw IllegalArgumentException("No auth type with raw type = $rawType.")
        }
    }
}
