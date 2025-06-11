

package com.duckduckgo.app.di

import com.duckduckgo.app.global.db.AppDatabase
import dagger.Module
import dagger.Provides

@Module
object DaoModule {

    @Provides
    fun providesTdsTrackDao(database: AppDatabase) = database.tdsTrackerDao()

    @Provides
    fun providesTdsEntityDao(database: AppDatabase) = database.tdsEntityDao()

    @Provides
    fun providesTdsDomainEntityDao(database: AppDatabase) = database.tdsDomainEntityDao()

    @Provides
    fun providesTdsCnameEntityDao(database: AppDatabase) = database.tdsCnameEntityDao()

    @Provides
    fun providesUserAllowList(database: AppDatabase) = database.userAllowListDao()

    @Provides
    fun providesNetworkLeaderboardDao(database: AppDatabase) = database.networkLeaderboardDao()

    @Provides
    fun providesBookmarksDao(database: AppDatabase) = database.bookmarksDao()

    @Provides
    fun providesFavoritesDao(database: AppDatabase) = database.favoritesDao()

    @Provides
    fun providesBookmarkFoldersDao(database: AppDatabase) = database.bookmarkFoldersDao()

    @Provides
    fun providesTabsDao(database: AppDatabase) = database.tabsDao()

    @Provides
    fun surveyDao(database: AppDatabase) = database.surveyDao()

    @Provides
    fun dismissedCtaDao(database: AppDatabase) = database.dismissedCtaDao()

    @Provides
    fun searchCountDao(database: AppDatabase) = database.searchCountDao()

    @Provides
    fun appDaysUsedDao(database: AppDatabase) = database.appsDaysUsedDao()

    @Provides
    fun notification(database: AppDatabase) = database.notificationDao()

    @Provides
    fun privacyProtectionCounts(database: AppDatabase) = database.privacyProtectionCountsDao()

    @Provides
    fun tdsDao(database: AppDatabase) = database.tdsDao()

    @Provides
    fun userStageDao(database: AppDatabase) = database.userStageDao()

    @Provides
    fun fireproofWebsiteDao(database: AppDatabase) = database.fireproofWebsiteDao()

    @Provides
    fun userEventsDao(database: AppDatabase) = database.userEventsDao()

    @Provides
    fun locationPermissionsDao(database: AppDatabase) = database.locationPermissionsDao()

    @Provides
    fun webTrackersBlockedDao(database: AppDatabase) = database.webTrackersBlockedDao()

    @Provides
    fun allowedDomainsDao(database: AppDatabase) = database.authCookiesAllowedDomainsDao()

    @Provides
    fun syncEntitiesDao(database: AppDatabase) = database.syncEntitiesDao()

    @Provides
    fun syncRelationsDao(database: AppDatabase) = database.syncRelationsDao()
}
