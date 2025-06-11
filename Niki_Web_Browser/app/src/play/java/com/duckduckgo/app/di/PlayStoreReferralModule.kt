

package com.duckduckgo.app.di

import com.duckduckgo.app.referral.*
import com.duckduckgo.app.statistics.AtbInitializerListener
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.referral.PlayStoreAppReferrerStateListener
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import dagger.multibindings.IntoSet

@Module
@ContributesTo(
    scope = AppScope::class,
    replaces = [StoreReferralModule::class],
)
class PlayStoreReferralModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun appInstallationReferrerStateListener(
        playStoreAppReferrerStateListener: PlayStoreAppReferrerStateListener,
    ): AppInstallationReferrerStateListener = playStoreAppReferrerStateListener

    @Provides
    @IntoSet
    fun providedReferrerAtbInitializerListener(
        playStoreAppReferrerStateListener: PlayStoreAppReferrerStateListener,
    ): AtbInitializerListener = playStoreAppReferrerStateListener
}
