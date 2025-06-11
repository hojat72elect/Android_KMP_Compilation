package ca.hojat.gamehub.core.domain.common.usecases

interface UseCase<In, Out> {
    suspend fun execute(params: In): Out
}

suspend fun <Out> UseCase<Unit, Out>.execute(): Out {
    return execute(Unit)
}
