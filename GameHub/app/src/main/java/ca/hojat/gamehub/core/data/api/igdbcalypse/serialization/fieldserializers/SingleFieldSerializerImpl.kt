package ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.fieldserializers

import ca.hojat.gamehub.core.data.api.igdbcalypse.Constants

class SingleFieldSerializerImpl(
    private val fieldChain: List<String>
) : FieldSerializer {

    override fun serialize(): String {
        return fieldChain.joinToString(Constants.HIERARCHICAL_FIELD_SEPARATOR)
    }
}
