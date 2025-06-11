

package com.duckduckgo.app.di

import com.duckduckgo.app.referral.*
import com.duckduckgo.app.statistics.AtbInitializerListener
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import dagger.multibindings.IntoSet

@Module
@ContributesTo(AppScope::class)
object StoreReferralModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun appInstallationReferrerStateListener(
        referrerStateListener: EmptyReferrerStateListener,
    ): AppInstallationReferrerStateListener = referrerStateListener

    @Provides
    @IntoSet
    fun providedReferrerAtbInitializerListener(
        referrerStateListener: EmptyReferrerStateListener,
    ): AtbInitializerListener = referrerStateListener
}
