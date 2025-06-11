package ca.hojat.gamehub.core.data.database.common.di

import ca.hojat.gamehub.core.data.database.GameNewsDatabase
import ca.hojat.gamehub.core.data.database.articles.ArticlesTable
import ca.hojat.gamehub.core.data.database.games.tables.GamesTable
import ca.hojat.gamehub.core.data.database.games.tables.LikedGamesTable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TablesModule {

    @Provides
    @Singleton
    fun provideGamesTable(gameNewsDatabase: GameNewsDatabase): GamesTable {
        return gameNewsDatabase.gamesTable
    }

    @Provides
    @Singleton
    fun provideLikedGamesTable(gameNewsDatabase: GameNewsDatabase): LikedGamesTable {
        return gameNewsDatabase.likedGamesTable
    }

    @Provides
    @Singleton
    fun provideArticlesTable(gameNewsDatabase: GameNewsDatabase): ArticlesTable {
        return gameNewsDatabase.articlesTable
    }
}
