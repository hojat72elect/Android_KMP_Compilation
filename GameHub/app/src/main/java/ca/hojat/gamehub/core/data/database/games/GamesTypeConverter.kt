package ca.hojat.gamehub.core.data.database.games

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import ca.hojat.gamehub.core.data.JsonConverter
import ca.hojat.gamehub.core.data.database.common.RoomTypeConverter
import ca.hojat.gamehub.core.data.database.games.entities.DbAgeRating
import ca.hojat.gamehub.core.data.database.games.entities.DbCategory
import ca.hojat.gamehub.core.data.database.games.entities.DbGenre
import ca.hojat.gamehub.core.data.database.games.entities.DbImage
import ca.hojat.gamehub.core.data.database.games.entities.DbInvolvedCompany
import ca.hojat.gamehub.core.data.database.games.entities.DbKeyword
import ca.hojat.gamehub.core.data.database.games.entities.DbMode
import ca.hojat.gamehub.core.data.database.games.entities.DbPlatform
import ca.hojat.gamehub.core.data.database.games.entities.DbPlayerPerspective
import ca.hojat.gamehub.core.data.database.games.entities.DbReleaseDate
import ca.hojat.gamehub.core.data.database.games.entities.DbTheme
import ca.hojat.gamehub.core.data.database.games.entities.DbVideo
import ca.hojat.gamehub.core.data.database.games.entities.DbWebsite
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@ProvidedTypeConverter
@BindType(contributesTo = BindType.Collection.SET)
@Suppress("TooManyFunctions")
class GamesTypeConverter @Inject constructor(
    private val jsonConverter: JsonConverter
) : RoomTypeConverter {

    @TypeConverter
    fun fromCategory(category: DbCategory): String {
        return jsonConverter.toJson(category)
    }

    @TypeConverter
    fun toCategory(json: String): DbCategory {
        return (jsonConverter.fromJson(json) ?: DbCategory.UNKNOWN)
    }

    @TypeConverter
    fun fromImage(image: DbImage?): String {
        return jsonConverter.toJson(image)
    }

    @TypeConverter
    fun toImage(json: String): DbImage? {
        return jsonConverter.fromJson(json)
    }

    @TypeConverter
    fun fromImages(images: List<DbImage>): String {
        return jsonConverter.toJson(images)
    }

    @TypeConverter
    fun toImages(json: String): List<DbImage> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromReleaseDates(releaseDates: List<DbReleaseDate>): String {
        return jsonConverter.toJson(releaseDates)
    }

    @TypeConverter
    fun toReleaseDates(json: String): List<DbReleaseDate> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromAgeRatings(ageRatings: List<DbAgeRating>): String {
        return jsonConverter.toJson(ageRatings)
    }

    @TypeConverter
    fun toAgeRatings(json: String): List<DbAgeRating> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromVideos(videos: List<DbVideo>): String {
        return jsonConverter.toJson(videos)
    }

    @TypeConverter
    fun toVideos(json: String): List<DbVideo> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromGenres(genres: List<DbGenre>): String {
        return jsonConverter.toJson(genres)
    }

    @TypeConverter
    fun toGenres(json: String): List<DbGenre> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromPlatforms(platforms: List<DbPlatform>): String {
        return jsonConverter.toJson(platforms)
    }

    @TypeConverter
    fun toPlatforms(json: String): List<DbPlatform> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromPlayerPerspectives(playerPerspectives: List<DbPlayerPerspective>): String {
        return jsonConverter.toJson(playerPerspectives)
    }

    @TypeConverter
    fun toPlayerPerspectives(json: String): List<DbPlayerPerspective> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromThemes(themes: List<DbTheme>): String {
        return jsonConverter.toJson(themes)
    }

    @TypeConverter
    fun toThemes(json: String): List<DbTheme> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromModes(modes: List<DbMode>): String {
        return jsonConverter.toJson(modes)
    }

    @TypeConverter
    fun toModes(json: String): List<DbMode> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromKeywords(keywords: List<DbKeyword>): String {
        return jsonConverter.toJson(keywords)
    }

    @TypeConverter
    fun toKeywords(json: String): List<DbKeyword> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromInvolvedCompanies(involvedCompanies: List<DbInvolvedCompany>): String {
        return jsonConverter.toJson(involvedCompanies)
    }

    @TypeConverter
    fun toInvolvedCompanies(json: String): List<DbInvolvedCompany> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromWebsites(websites: List<DbWebsite>): String {
        return jsonConverter.toJson(websites)
    }

    @TypeConverter
    fun toWebsites(json: String): List<DbWebsite> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }

    @TypeConverter
    fun fromSimilarGames(similarGames: List<Int>): String {
        return jsonConverter.toJson(similarGames)
    }

    @TypeConverter
    fun toSimilarGames(json: String): List<Int> {
        return (jsonConverter.fromJson(json) ?: emptyList())
    }
}
