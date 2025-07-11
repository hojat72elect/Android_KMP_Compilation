

package com.duckduckgo.app.di

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import com.duckduckgo.app.fire.FireAnimationLoader
import com.duckduckgo.app.fire.LottieFireAnimationLoader
import com.duckduckgo.app.global.shortcut.AppShortcutCreator
import com.duckduckgo.app.icon.api.AppIconModifier
import com.duckduckgo.app.icon.api.IconModifier
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.app.systemsearch.DeviceAppListProvider
import com.duckduckgo.app.systemsearch.DeviceAppLookup
import com.duckduckgo.app.systemsearch.InstalledDeviceAppListProvider
import com.duckduckgo.app.systemsearch.InstalledDeviceAppLookup
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineScope

@Module
object SystemComponentsModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun packageManager(context: Context): PackageManager = context.packageManager

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun locationManager(context: Context): LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun deviceAppsListProvider(packageManager: PackageManager): DeviceAppListProvider = InstalledDeviceAppListProvider(packageManager)

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun deviceAppLookup(deviceAppListProvider: DeviceAppListProvider): DeviceAppLookup = InstalledDeviceAppLookup(deviceAppListProvider)

    @Provides
    fun appIconModifier(
        context: Context,
        appShortcutCreator: AppShortcutCreator,
        appBuildConfig: AppBuildConfig,
    ): IconModifier =
        AppIconModifier(context, appShortcutCreator, appBuildConfig)

    @Provides
    fun animatorLoader(
        context: Context,
        settingsDataStore: SettingsDataStore,
        dispatcherProvider: DispatcherProvider,
        @AppCoroutineScope appCoroutineScope: CoroutineScope,
    ): FireAnimationLoader {
        return LottieFireAnimationLoader(context, settingsDataStore, dispatcherProvider, appCoroutineScope)
    }
}

@Module
@ContributesTo(AppScope::class)
abstract class SystemComponentsModuleBindings {
    @Binds
    @IntoSet
    abstract fun animatorLoaderObserver(fireAnimationLoader: FireAnimationLoader): MainProcessLifecycleObserver
}
