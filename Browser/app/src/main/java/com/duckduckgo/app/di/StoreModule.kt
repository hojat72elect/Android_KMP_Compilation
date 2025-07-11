

package com.duckduckgo.app.di

import com.duckduckgo.app.fire.UnsentForgetAllPixelStore
import com.duckduckgo.app.fire.UnsentForgetAllPixelStoreSharedPreferences
import com.duckduckgo.app.global.events.db.AppUserEventsStore
import com.duckduckgo.app.global.events.db.UserEventsStore
import com.duckduckgo.app.global.install.AppInstallSharedPreferences
import com.duckduckgo.app.global.install.AppInstallStore
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.onboarding.store.AppUserStageStore
import com.duckduckgo.app.onboarding.store.OnboardingStore
import com.duckduckgo.app.onboarding.store.OnboardingStoreImpl
import com.duckduckgo.app.onboarding.store.UserStageStore
import com.duckduckgo.app.statistics.store.StatisticsDataStore
import com.duckduckgo.app.statistics.store.StatisticsSharedPreferences
import com.duckduckgo.app.tabs.db.TabsDbSanitizer
import com.duckduckgo.app.tabs.model.TabDataRepository
import com.duckduckgo.app.tabs.model.TabRepository
import com.duckduckgo.app.widget.FavoritesObserver
import com.duckduckgo.common.ui.store.ThemingDataStore
import com.duckduckgo.common.ui.store.ThemingSharedPreferences
import com.duckduckgo.widget.AppWidgetThemePreferences
import com.duckduckgo.widget.WidgetPreferences
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class StoreModule {

    @Binds
    abstract fun bindStatisticsStore(statisticsStore: StatisticsSharedPreferences): StatisticsDataStore

    @Binds
    abstract fun bindThemingStore(themeDataStore: ThemingSharedPreferences): ThemingDataStore

    @Binds
    abstract fun bindOnboardingStore(onboardingStore: OnboardingStoreImpl): OnboardingStore

    @Binds
    abstract fun bindTabRepository(tabRepository: TabDataRepository): TabRepository

    @Binds
    abstract fun bindAppInstallStore(store: AppInstallSharedPreferences): AppInstallStore

    @Binds
    @IntoSet
    abstract fun bindAppInstallStoreObserver(appInstallStore: AppInstallStore): MainProcessLifecycleObserver

    @Binds
    abstract fun bindDataClearingStore(store: UnsentForgetAllPixelStoreSharedPreferences): UnsentForgetAllPixelStore

    @Binds
    abstract fun bindUserStageStore(userStageStore: AppUserStageStore): UserStageStore

    @Binds
    abstract fun bindUserEventsStore(userEventsStore: AppUserEventsStore): UserEventsStore

    @Binds
    @IntoSet
    abstract fun bindTabsDbSanitizerObserver(tabsDbSanitizer: TabsDbSanitizer): MainProcessLifecycleObserver

    @Binds
    @IntoSet
    abstract fun bindFavoritesObserver(favoritesObserver: FavoritesObserver): MainProcessLifecycleObserver

    @Binds
    abstract fun bindWidgetPreferences(store: AppWidgetThemePreferences): WidgetPreferences
}
