package ca.hojat.gamehub.core.data.database.articles

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import ca.hojat.gamehub.core.data.JsonConverter
import ca.hojat.gamehub.core.data.database.common.RoomTypeConverter
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@ProvidedTypeConverter
@BindType(contributesTo = BindType.Collection.SET)
class ArticlesTypeConverter @Inject constructor(
    private val jsonConverter: JsonConverter
) : RoomTypeConverter {

    @TypeConverter
    fun fromImageUrls(imageUrls: Map<DbImageType, String>): String {
        return jsonConverter.toJson(imageUrls)
    }

    @TypeConverter
    fun toImageUrls(json: String): Map<DbImageType, String> {
        return (jsonConverter.fromJson(json) ?: emptyMap())
    }
}
