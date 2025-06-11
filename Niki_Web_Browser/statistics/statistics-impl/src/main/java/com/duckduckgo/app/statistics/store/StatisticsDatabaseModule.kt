

package com.duckduckgo.app.statistics.store

import android.content.Context
import androidx.room.Room
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
class StatisticsDatabaseModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideStatisticsDatabase(context: Context): StatisticsDatabase =
        Room
            .databaseBuilder(
                context = context,
                klass = StatisticsDatabase::class.java,
                name = "pixels.db",
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideDailyPixelFiredDao(db: StatisticsDatabase): DailyPixelFiredDao =
        db.dailyPixelFiredDao()

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideUniquePixelFiredDao(db: StatisticsDatabase): UniquePixelFiredDao =
        db.uniquePixelFiredDao()
}
