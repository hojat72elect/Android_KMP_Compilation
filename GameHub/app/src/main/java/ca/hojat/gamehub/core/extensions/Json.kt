package ca.hojat.gamehub.core.extensions

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

inline fun <reified T> Json.decodeFromStringOrNull(json: String): T? {
    return try {
        decodeFromString(json)
    } catch (ignore: Throwable) {
        null
    }
}
