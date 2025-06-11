package ca.hojat.gamehub.core.domain.common.usecases

import kotlinx.coroutines.flow.Flow

/**
 * After executing these UseCases, you will receive a Flow of data.
 */
interface ObservableUseCase<In, Out> {
    fun execute(params: In): Flow<Out>
}

fun <Out> ObservableUseCase<Unit, Out>.execute(): Flow<Out> {
    return execute(Unit)
}
