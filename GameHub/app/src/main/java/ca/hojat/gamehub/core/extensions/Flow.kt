package ca.hojat.gamehub.core.extensions

import ca.hojat.gamehub.core.domain.DomainResult
import ca.hojat.gamehub.core.domain.entities.DomainException
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun <T> Flow<DomainResult<T>>.onEachSuccess(action: suspend (T) -> Unit): Flow<DomainResult<T>> {
    return onEach { it.onSuccess(action) }
}

fun <T> Flow<DomainResult<T>>.resultOrError(): Flow<T> {
    return map {
        if (it is Ok) return@map it.value
        if (it is Err) throw DomainException(it.error)

        error("The result is neither Ok nor Err.")
    }
}

fun <S1, S2, E1> Flow<Result<S1, E1>>.mapSuccess(
    success: (S1) -> S2
): Flow<Result<S2, E1>> {
    return map { it.map(success) }
}


data class Tuple4<T1, T2, T3, T4>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4
)

fun <T1, T2, T3, T4> combine(
    f1: Flow<T1>,
    f2: Flow<T2>,
    f3: Flow<T3>,
    f4: Flow<T4>
): Flow<Tuple4<T1, T2, T3, T4>> {
    return kotlinx.coroutines.flow.combine(f1, f2, f3, f4) { t1, t2, t3, t4 ->
        Tuple4(t1, t2, t3, t4)
    }
}

fun <T> Flow<T>.onError(action: suspend FlowCollector<T>.(cause: Throwable) -> Unit): Flow<T> =
    catch(action)

