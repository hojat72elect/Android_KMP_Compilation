package ca.hojat.gamehub.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.hojat.gamehub.core.data.database.articles.ArticlesTable
import ca.hojat.gamehub.core.data.database.articles.ArticlesTypeConverter
import ca.hojat.gamehub.core.data.database.articles.DbArticle
import ca.hojat.gamehub.core.data.database.games.GamesTypeConverter
import ca.hojat.gamehub.core.data.database.games.entities.DbGame
import ca.hojat.gamehub.core.data.database.games.entities.DbLikedGame
import ca.hojat.gamehub.core.data.database.games.tables.GamesTable
import ca.hojat.gamehub.core.data.database.games.tables.LikedGamesTable

private const val VERSION = 2

@Database(
    entities = [
        DbGame::class,
        DbLikedGame::class,
        DbArticle::class
    ],
    version = VERSION,
    // if someday, I was making a big change to database, I need to set the exportSchema to true
    // and also try to devise a migration plan (but that's not needed for now)
    exportSchema = false,
)
// Seems really strange that I have to specify this annotation here
// with custom provided type converters
@TypeConverters(
    GamesTypeConverter::class,
    ArticlesTypeConverter::class
)
abstract class GameNewsDatabase : RoomDatabase() {
    abstract val gamesTable: GamesTable
    abstract val likedGamesTable: LikedGamesTable
    abstract val articlesTable: ArticlesTable
}
