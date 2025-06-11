package ca.hojat.gamehub.core.data.api.igdbcalypse.serialization

object ApicalypseSerializerFactory {

    fun create(): ApicalypseSerializer {
        return ApicalypseSerializerImpl()
    }
}
