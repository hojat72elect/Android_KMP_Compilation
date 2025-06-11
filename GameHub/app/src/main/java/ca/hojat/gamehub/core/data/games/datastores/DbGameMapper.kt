package ca.hojat.gamehub.core.data.games.datastores

import ca.hojat.gamehub.core.data.database.games.entities.DbAgeRating
import ca.hojat.gamehub.core.data.database.games.entities.DbAgeRatingCategory
import ca.hojat.gamehub.core.data.database.games.entities.DbAgeRatingType
import ca.hojat.gamehub.core.data.database.games.entities.DbCategory
import ca.hojat.gamehub.core.data.database.games.entities.DbCompany
import ca.hojat.gamehub.core.data.database.games.entities.DbGame
import ca.hojat.gamehub.core.data.database.games.entities.DbGenre
import ca.hojat.gamehub.core.data.database.games.entities.DbImage
import ca.hojat.gamehub.core.data.database.games.entities.DbInvolvedCompany
import ca.hojat.gamehub.core.data.database.games.entities.DbKeyword
import ca.hojat.gamehub.core.data.database.games.entities.DbMode
import ca.hojat.gamehub.core.data.database.games.entities.DbPlatform
import ca.hojat.gamehub.core.data.database.games.entities.DbPlayerPerspective
import ca.hojat.gamehub.core.data.database.games.entities.DbReleaseDate
import ca.hojat.gamehub.core.data.database.games.entities.DbReleaseDateCategory
import ca.hojat.gamehub.core.data.database.games.entities.DbTheme
import ca.hojat.gamehub.core.data.database.games.entities.DbVideo
import ca.hojat.gamehub.core.data.database.games.entities.DbWebsite
import ca.hojat.gamehub.core.data.database.games.entities.DbWebsiteCategory
import ca.hojat.gamehub.core.domain.entities.AgeRating
import ca.hojat.gamehub.core.domain.entities.AgeRatingCategory
import ca.hojat.gamehub.core.domain.entities.AgeRatingType
import ca.hojat.gamehub.core.domain.entities.Category
import ca.hojat.gamehub.core.domain.entities.Company
import ca.hojat.gamehub.core.domain.entities.Game
import ca.hojat.gamehub.core.domain.entities.Genre
import ca.hojat.gamehub.core.domain.entities.Image
import ca.hojat.gamehub.core.domain.entities.InvolvedCompany
import ca.hojat.gamehub.core.domain.entities.Keyword
import ca.hojat.gamehub.core.domain.entities.Mode
import ca.hojat.gamehub.core.domain.entities.Platform
import ca.hojat.gamehub.core.domain.entities.PlayerPerspective
import ca.hojat.gamehub.core.domain.entities.ReleaseDate
import ca.hojat.gamehub.core.domain.entities.ReleaseDateCategory
import ca.hojat.gamehub.core.domain.entities.Theme
import ca.hojat.gamehub.core.domain.entities.Video
import ca.hojat.gamehub.core.domain.entities.Website
import ca.hojat.gamehub.core.domain.entities.WebsiteCategory
import javax.inject.Inject

class DbGameMapper @Inject constructor() {

    fun mapToDomainGame(dbGame: DbGame): Game {
        return Game(
            id = dbGame.id,
            followerCount = dbGame.followerCount,
            hypeCount = dbGame.hypeCount,
            releaseDate = dbGame.releaseDate,
            criticsRating = dbGame.criticsRating,
            usersRating = dbGame.usersRating,
            totalRating = dbGame.totalRating,
            name = dbGame.name,
            summary = dbGame.summary,
            storyline = dbGame.storyline,
            category = dbGame.category.toDomainCategory(),
            cover = dbGame.cover?.toDomainImage(),
            releaseDates = dbGame.releaseDates.toDomainReleaseDates(),
            ageRatings = dbGame.ageRatings.toDomainAgeRatings(),
            videos = dbGame.videos.toDomainVideos(),
            artworks = dbGame.artworks.toDomainImages(),
            screenshots = dbGame.screenshots.toDomainImages(),
            genres = dbGame.genres.toDomainGenres(),
            platforms = dbGame.platforms.toDomainPlatforms(),
            playerPerspectives = dbGame.playerPerspectives.toDomainPlayerPerspectives(),
            themes = dbGame.themes.toDomainThemes(),
            modes = dbGame.modes.toDomainModes(),
            keywords = dbGame.keywords.toDomainKeywords(),
            involvedCompanies = dbGame.involvedCompanies.toDomainInvolvedCompanies(),
            websites = dbGame.websites.toDomainWebsites(),
            similarGames = dbGame.similarGames,
        )
    }

    private fun DbCategory.toDomainCategory(): Category {
        return Category.valueOf(name)
    }

    private fun DbImage.toDomainImage(): Image {
        return Image(
            id = id,
            width = width,
            height = height,
        )
    }

    private fun List<DbImage>.toDomainImages(): List<Image> {
        return map { it.toDomainImage() }
    }

    private fun List<DbReleaseDate>.toDomainReleaseDates(): List<ReleaseDate> {
        return map {
            ReleaseDate(
                date = it.date,
                year = it.year,
                category = ReleaseDateCategory.valueOf(it.category.name),
            )
        }
    }

    private fun List<DbAgeRating>.toDomainAgeRatings(): List<AgeRating> {
        return map {
            AgeRating(
                category = AgeRatingCategory.valueOf(it.category.name),
                type = AgeRatingType.valueOf(it.type.name),
            )
        }
    }

    private fun List<DbVideo>.toDomainVideos(): List<Video> {
        return map {
            Video(
                id = it.id,
                name = it.name,
            )
        }
    }

    private fun List<DbGenre>.toDomainGenres(): List<Genre> {
        return map {
            Genre(
                name = it.name
            )
        }
    }

    private fun List<DbPlatform>.toDomainPlatforms(): List<Platform> {
        return map {
            Platform(
                abbreviation = it.abbreviation,
                name = it.name,
            )
        }
    }

    private fun List<DbPlayerPerspective>.toDomainPlayerPerspectives(): List<PlayerPerspective> {
        return map {
            PlayerPerspective(
                name = it.name,
            )
        }
    }

    private fun List<DbTheme>.toDomainThemes(): List<Theme> {
        return map {
            Theme(
                name = it.name,
            )
        }
    }

    private fun List<DbMode>.toDomainModes(): List<Mode> {
        return map {
            Mode(
                name = it.name,
            )
        }
    }

    private fun List<DbKeyword>.toDomainKeywords(): List<Keyword> {
        return map {
            Keyword(
                name = it.name,
            )
        }
    }

    private fun List<DbInvolvedCompany>.toDomainInvolvedCompanies(): List<InvolvedCompany> {
        return map {
            InvolvedCompany(
                company = it.company.toDomainCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting,
            )
        }
    }

    private fun DbCompany.toDomainCompany(): Company {
        return Company(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDomainImage(),
            developedGames = developedGames,
        )
    }

    private fun List<DbWebsite>.toDomainWebsites(): List<Website> {
        return map {
            Website(
                id = it.id,
                url = it.url,
                category = WebsiteCategory.valueOf(it.category.name),
            )
        }
    }

    fun mapToDatabaseGame(domainGame: Game): DbGame {
        return DbGame(
            id = domainGame.id,
            followerCount = domainGame.followerCount,
            hypeCount = domainGame.hypeCount,
            releaseDate = domainGame.releaseDate,
            criticsRating = domainGame.criticsRating,
            usersRating = domainGame.usersRating,
            totalRating = domainGame.totalRating,
            name = domainGame.name,
            summary = domainGame.summary,
            storyline = domainGame.storyline,
            category = domainGame.category.toDbCategory(),
            cover = domainGame.cover?.toDbImage(),
            releaseDates = domainGame.releaseDates.toDbReleaseDates(),
            ageRatings = domainGame.ageRatings.toDbAgeRatings(),
            videos = domainGame.videos.toDbVideos(),
            artworks = domainGame.artworks.toDbImages(),
            screenshots = domainGame.screenshots.toDbImages(),
            genres = domainGame.genres.toDbGenres(),
            platforms = domainGame.platforms.toDbPlatforms(),
            playerPerspectives = domainGame.playerPerspectives.toDbPlayerPerspectives(),
            themes = domainGame.themes.toDbThemes(),
            modes = domainGame.modes.toDbModes(),
            keywords = domainGame.keywords.toDbKeywords(),
            involvedCompanies = domainGame.involvedCompanies.toDatabaseInvolvedCompanies(),
            websites = domainGame.websites.toDbWebsites(),
            similarGames = domainGame.similarGames,
        )
    }

    private fun Category.toDbCategory(): DbCategory {
        return DbCategory.valueOf(name)
    }

    private fun Image.toDbImage(): DbImage {
        return DbImage(
            id = id,
            width = width,
            height = height,
        )
    }

    private fun List<Image>.toDbImages(): List<DbImage> {
        return map { it.toDbImage() }
    }

    private fun List<ReleaseDate>.toDbReleaseDates(): List<DbReleaseDate> {
        return map {
            DbReleaseDate(
                date = it.date,
                year = it.year,
                category = DbReleaseDateCategory.valueOf(it.category.name),
            )
        }
    }

    private fun List<AgeRating>.toDbAgeRatings(): List<DbAgeRating> {
        return map {
            DbAgeRating(
                category = DbAgeRatingCategory.valueOf(it.category.name),
                type = DbAgeRatingType.valueOf(it.type.name),
            )
        }
    }

    private fun List<Video>.toDbVideos(): List<DbVideo> {
        return map {
            DbVideo(
                id = it.id,
                name = it.name,
            )
        }
    }

    private fun List<Genre>.toDbGenres(): List<DbGenre> {
        return map {
            DbGenre(
                name = it.name,
            )
        }
    }

    private fun List<Platform>.toDbPlatforms(): List<DbPlatform> {
        return map {
            DbPlatform(
                abbreviation = it.abbreviation,
                name = it.name,
            )
        }
    }

    private fun List<PlayerPerspective>.toDbPlayerPerspectives(): List<DbPlayerPerspective> {
        return map {
            DbPlayerPerspective(
                name = it.name,
            )
        }
    }

    private fun List<Theme>.toDbThemes(): List<DbTheme> {
        return map {
            DbTheme(
                name = it.name,
            )
        }
    }

    private fun List<Mode>.toDbModes(): List<DbMode> {
        return map {
            DbMode(
                name = it.name,
            )
        }
    }

    private fun List<Keyword>.toDbKeywords(): List<DbKeyword> {
        return map {
            DbKeyword(
                name = it.name,
            )
        }
    }

    private fun List<InvolvedCompany>.toDatabaseInvolvedCompanies(): List<DbInvolvedCompany> {
        return map {
            DbInvolvedCompany(
                company = it.company.toDbCompany(),
                isDeveloper = it.isDeveloper,
                isPublisher = it.isPublisher,
                isPorter = it.isPorter,
                isSupporting = it.isSupporting,
            )
        }
    }

    private fun Company.toDbCompany(): DbCompany {
        return DbCompany(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            logo = logo?.toDbImage(),
            developedGames = developedGames,
        )
    }

    private fun List<Website>.toDbWebsites(): List<DbWebsite> {
        return map {
            DbWebsite(
                id = it.id,
                url = it.url,
                category = DbWebsiteCategory.valueOf(it.category.name),
            )
        }
    }

    fun mapToDomainGames(dbGames: List<DbGame>): List<Game> {
        return dbGames.map(::mapToDomainGame)
    }

    fun mapToDatabaseGames(domainGames: List<Game>): List<DbGame> {
        return domainGames.map(::mapToDatabaseGame)
    }
}
