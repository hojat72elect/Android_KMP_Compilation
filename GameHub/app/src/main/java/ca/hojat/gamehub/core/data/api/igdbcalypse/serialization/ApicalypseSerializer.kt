package ca.hojat.gamehub.core.data.api.igdbcalypse.serialization

interface ApicalypseSerializer {
    fun serialize(clazz: Class<*>): String
}
