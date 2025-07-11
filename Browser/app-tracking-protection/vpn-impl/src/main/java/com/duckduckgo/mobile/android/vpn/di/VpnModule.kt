

package com.duckduckgo.mobile.android.vpn.di

import android.content.pm.PackageManager
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.processor.requestingapp.*
import com.duckduckgo.mobile.android.vpn.store.*
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(VpnScope::class)
object VpnModule {

    @SingleInstanceIn(VpnScope::class)
    @Provides
    fun providesAppNameResolver(packageManager: PackageManager): AppNameResolver = RealAppNameResolver(packageManager)

    @Provides
    fun providesDispatcherProvider(): VpnDispatcherProvider {
        return DefaultVpnDispatcherProvider()
    }

    @Provides
    fun provideRuntime(): Runtime {
        return Runtime.getRuntime()
    }
}
