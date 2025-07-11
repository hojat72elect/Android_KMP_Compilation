

package com.duckduckgo.app.di

import android.app.Application
import com.duckduckgo.app.browser.certificates.CertificateTrustedStoreModule
import com.duckduckgo.app.browser.di.BrowserModule
import com.duckduckgo.app.browser.favicon.FaviconModule
import com.duckduckgo.app.browser.rating.di.RatingModule
import com.duckduckgo.app.email.di.EmailModule
import com.duckduckgo.app.global.DuckDuckGoApplication
import com.duckduckgo.app.onboarding.di.OnboardingModule
import com.duckduckgo.app.surrogates.di.ResourceSurrogateModule
import com.duckduckgo.app.usage.di.AppUsageModule
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.widget.EmptyFavoritesWidgetService
import com.duckduckgo.widget.FavoritesWidgetService
import com.duckduckgo.widget.SearchAndFavoritesWidget
import com.duckduckgo.widget.SearchWidget
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import dagger.SingleInstanceIn
import dagger.android.AndroidInjector
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit

@SingleInstanceIn(AppScope::class)
@MergeComponent(
    scope = AppScope::class,
    modules = [
        ApplicationModule::class,
        WorkerModule::class,
        NetworkModule::class,
        AppConfigurationDownloaderModule::class,
        StoreModule::class,
        DatabaseModule::class,
        DaoModule::class,
        JsonModule::class,
        SystemComponentsModule::class,
        BrowserModule::class,
        ResourceSurrogateModule::class,
        NotificationModule::class,
        OnboardingModule::class,
        FaviconModule::class,
        PrivacyModule::class,
        WidgetModule::class,
        RatingModule::class,
        AppUsageModule::class,
        FileModule::class,
        CoroutinesModule::class,
        CertificateTrustedStoreModule::class,
        FormatterModule::class,
        EmailModule::class,
    ],
)
interface AppComponent : AndroidInjector<DuckDuckGoApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun applicationCoroutineScope(@AppCoroutineScope applicationCoroutineScope: CoroutineScope): Builder

        fun build(): AppComponent
    }

    fun inject(searchWidget: SearchWidget)

    fun inject(searchAndFavsWidget: SearchAndFavoritesWidget)

    fun inject(favoritesWidgetItemFactory: FavoritesWidgetService.FavoritesWidgetItemFactory)

    fun inject(emptyFavoritesWidgetItemFactory: EmptyFavoritesWidgetService.EmptyFavoritesWidgetItemFactory)

    // accessor to Retrofit instance for test only only for test
    @Named("api")
    fun retrofit(): Retrofit
}
