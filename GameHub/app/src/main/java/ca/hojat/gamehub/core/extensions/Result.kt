package ca.hojat.gamehub.core.extensions

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

fun <T> T.asSuccess(): Ok<T> = Ok(this)

suspend fun <V, E> Result<V, E>.onSuccess(action: suspend (V) -> Unit): Result<V, E> {
    if (this is Ok) {
        action(value)
    }

    return this
}
