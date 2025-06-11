package ca.hojat.gamehub.core.data.api.igdbcalypse.serialization.fieldserializers

object FieldSerializerFactory {

    fun create(
        fieldChain: List<String>,
        children: List<FieldSerializer>
    ): FieldSerializer {
        return if (children.isEmpty()) {
            SingleFieldSerializerImpl(fieldChain)
        } else {
            CompositeFieldSerializer(children)
        }
    }
}
