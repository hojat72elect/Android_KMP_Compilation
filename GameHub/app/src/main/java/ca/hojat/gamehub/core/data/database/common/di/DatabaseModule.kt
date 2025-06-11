package ca.hojat.gamehub.core.data.database.common.di

import android.content.Context
import androidx.room.Room
import ca.hojat.gamehub.core.data.database.GameNewsDatabase
import ca.hojat.gamehub.core.data.database.common.MIGRATIONS
import ca.hojat.gamehub.core.data.database.common.RoomTypeConverter
import ca.hojat.gamehub.core.data.database.common.addTypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val DATABASE_NAME = "gamenews.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    @Suppress("SpreadOperator")
    fun provideGameNewsDatabase(
        @ApplicationContext context: Context,
        typeConverters: Set<@JvmSuppressWildcards RoomTypeConverter>
    ): GameNewsDatabase {
        return Room.databaseBuilder(
            context,
            GameNewsDatabase::class.java,
            DATABASE_NAME
        )
            .addTypeConverters(typeConverters)
            .addMigrations(*MIGRATIONS)
            .build()
    }
}
