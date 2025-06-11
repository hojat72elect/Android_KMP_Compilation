package com.duckduckgo.app.flipper

import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.DaggerSet
import com.duckduckgo.di.scopes.AppScope
import com.facebook.flipper.core.FlipperPlugin
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import dagger.multibindings.Multibinds

private class FlipperPluginPluginPoint(
    private val plugins: DaggerSet<FlipperPlugin>,
) : PluginPoint<FlipperPlugin> {
    override fun getPlugins(): Collection<FlipperPlugin> {
        return plugins
    }
}

@Module
@ContributesTo(AppScope::class)
abstract class FlipperPluginModule {
    @Multibinds
    abstract fun bindEmptySettingInternalFeaturePlugins(): DaggerSet<FlipperPlugin>

    @Module
    @ContributesTo(AppScope::class)
    class SettingInternalFeaturePluginModuleExt {
        @Provides
        @SingleInstanceIn(AppScope::class)
        fun provideSettingInternalFeaturePlugins(
            plugins: DaggerSet<FlipperPlugin>,
        ): PluginPoint<FlipperPlugin> {
            return FlipperPluginPluginPoint(plugins)
        }
    }
}
