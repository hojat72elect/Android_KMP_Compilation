package ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.fieldserializers

import ca.hojat.gamehub.core.data.api.igdbcalypse.Constants

class CompositeFieldSerializer(
    private val children: List<FieldSerializer>
) : FieldSerializer {

    override fun serialize(): String {
        return buildList {
            for (child in children) {
                add(child.serialize())
            }
        }
            .joinToString(Constants.FIELD_SEPARATOR)
    }
}
